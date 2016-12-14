/**
  * Copyright 2014 Dropbox, Inc.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *    http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package djinni

import djinni.ast.Record.DerivingType
import djinni.ast._
import djinni.generatorTools._
import djinni.meta._
import djinni.writer.IndentWriter

import scala.collection.mutable

class CppGenerator(spec: Spec) extends Generator(spec) {

  val marshal = new CppMarshal(spec)

  val writeCppFile = writeCppFileGeneric(spec.cppOutFolder.get, spec.cppNamespace, spec.cppFileIdentStyle, spec.cppIncludePrefix) _
  def writeHppFile(name: String, origin: String, includes: Iterable[String], fwds: Iterable[String], f: IndentWriter => Unit, f2: IndentWriter => Unit = (w => {})) =
    writeHppFileGeneric(spec.cppHeaderOutFolder.get, spec.cppNamespace, spec.cppFileIdentStyle)(name, origin, includes, fwds, f, f2)

  class CppRefs(name: String) {
    var hpp = mutable.TreeSet[String]()
    var hppFwds = mutable.TreeSet[String]()
    var cpp = mutable.TreeSet[String]()

    def find(ty: TypeRef, forwardDeclareOnly: Boolean) { find(ty.resolved, forwardDeclareOnly) }
    def find(tm: MExpr, forwardDeclareOnly: Boolean) {
      tm.args.foreach((x) => find(x, forwardDeclareOnly))
      find(tm.base, forwardDeclareOnly)
    }
    def find(m: Meta, forwardDeclareOnly : Boolean) = {
      for(r <- marshal.hppReferences(m, name, forwardDeclareOnly)) r match {
        case ImportRef(arg) => hpp.add("#include " + arg)
        case DeclRef(decl, Some(spec.cppNamespace)) => hppFwds.add(decl)
        case DeclRef(_, _) =>
      }
      for(r <- marshal.cppReferences(m, name, forwardDeclareOnly)) r match {
        case ImportRef(arg) => cpp.add("#include " + arg)
        case DeclRef(_, _) =>
      }
    }
  }

  override def generateEnum(origin: String, ident: Ident, doc: Doc, e: Enum) {
    val refs = new CppRefs(ident.name)
    val self = marshal.typename(ident, e)

    if (spec.cppEnumHashWorkaround) {
      refs.hpp.add("#include <functional>") // needed for std::hash
    }

    writeHppFile(ident, origin, refs.hpp, refs.hppFwds, w => {
      w.w(s"enum class $self : int").bracedSemi {
        for (o <- e.options) {
          writeDoc(w, o.doc)
          w.wl(idCpp.enum(o.ident.name) + ",")
        }
      }
    },
    w => {
      // std::hash specialization has to go *outside* of the wrapNs
      if (spec.cppEnumHashWorkaround) {
        val fqSelf = marshal.fqTypename(ident, e)
        w.wl
        wrapNamespace(w, "std",
          (w: IndentWriter) => {
            w.wl("template <>")
            w.w(s"struct hash<$fqSelf>").bracedSemi {
              w.w(s"size_t operator()($fqSelf type) const").braced {
                w.wl("return std::hash<int>()(static_cast<int>(type));")
              }
            }
          }
        )
      }
    })
  }

  def generateHppConstants(w: IndentWriter, consts: Seq[Const]) = {
    for (c <- consts) {
      w.wl
      writeDoc(w, c.doc)
      w.wl(s"static ${marshal.fieldType(c.ty)} const ${idCpp.const(c.ident)};")
    }
  }

  def generateCppConstants(w: IndentWriter, consts: Seq[Const], selfName: String) = {
    def writeCppConst(w: IndentWriter, ty: TypeRef, v: Any): Unit = v match {
      case l: Long => w.w(l.toString)
      case d: Double if marshal.fieldType(ty) == "float" => w.w(d.toString + "f")
      case d: Double => w.w(d.toString)
      case b: Boolean => w.w(if (b) "true" else "false")
      case s: String => w.w("{" + s + "}")
      case e: EnumValue => w.w(marshal.typename(ty) + "::" + idCpp.enum(e.ty.name + "_" + e.name))
      case v: ConstRef => w.w(selfName + "::" + idCpp.const(v))
      case z: Map[_, _] => { // Value is record
        val recordMdef = ty.resolved.base.asInstanceOf[MDef]
        val record = recordMdef.body.asInstanceOf[Record]
        val vMap = z.asInstanceOf[Map[String, Any]]
        w.wl(marshal.typename(ty) + "(")
        w.increase()
        // Use exact sequence
        val skipFirst = SkipFirst()
        for (f <- record.fields) {
          skipFirst {w.wl(",")}
          writeCppConst(w, f.ty, vMap.apply(f.ident.name))
          w.w(" /* " + idCpp.field(f.ident) + " */ ")
        }
        w.w(")")
        w.decrease()
      }
    }

    val skipFirst = SkipFirst()
    for (c <- consts) {
      skipFirst{ w.wl }
      w.w(s"${marshal.fieldType(c.ty)} const $selfName::${idCpp.const(c.ident)} = ")
      writeCppConst(w, c.ty, c.value)
      w.wl(";")
    }
  }

  override def generateRecord(origin: String, ident: Ident, doc: Doc, params: Seq[TypeParam], r: Record) {
    val refs = new CppRefs(ident.name)
    r.fields.foreach(f => refs.find(f.ty, false))
    r.consts.foreach(c => refs.find(c.ty, false))
    refs.hpp.add("#include <utility>") // Add for std::move
    if (r.derivingTypes.contains(DerivingType.Json)) {
      refs.hpp.add("#include <json11/json11.hpp>")
    }
    val self = marshal.typename(ident, r)
    val (cppName, cppFinal) = if (r.ext.cpp) (ident.name + "_base", "") else (ident.name, " final")
    val actualSelf = marshal.typename(cppName, r)

    // Requiring the extended class
    if (r.ext.cpp) {
      refs.cpp.add("#include "+q(spec.cppExtendedRecordIncludePrefix + spec.cppFileIdentStyle(ident) + "." + spec.cppHeaderExt))
    }

    // C++ Header
    def writeCppPrototype(w: IndentWriter) {
      if (r.ext.cpp) {
        w.w(s"struct $self; // Requiring extended class")
        w.wl
        w.wl
      }
      writeDoc(w, doc)
      writeCppTypeParams(w, params)
      w.w("struct " + actualSelf + cppFinal).bracedSemi {
        generateHppConstants(w, r.consts)
        // Field definitions.
        for (f <- r.fields) {
          writeDoc(w, f.doc)
          w.wl(marshal.fieldType(f.ty) + " " + idCpp.field(f.ident) + ";")
        }

        if (r.derivingTypes.contains(DerivingType.Eq)) {
          w.wl
          w.wl(s"friend bool operator==(const $actualSelf& lhs, const $actualSelf& rhs);")
          w.wl(s"friend bool operator!=(const $actualSelf& lhs, const $actualSelf& rhs);")
        }
        if (r.derivingTypes.contains(DerivingType.Ord)) {
          w.wl
          w.wl(s"friend bool operator<(const $actualSelf& lhs, const $actualSelf& rhs);")
          w.wl(s"friend bool operator>(const $actualSelf& lhs, const $actualSelf& rhs);")
        }
        if (r.derivingTypes.contains(DerivingType.Eq) && r.derivingTypes.contains(DerivingType.Ord)) {
          w.wl
          w.wl(s"friend bool operator<=(const $actualSelf& lhs, const $actualSelf& rhs);")
          w.wl(s"friend bool operator>=(const $actualSelf& lhs, const $actualSelf& rhs);")
        }

        // Constructor.
        if(r.fields.nonEmpty) {
          w.wl
          writeAlignedCall(w, actualSelf + "(", r.fields, ")", f => marshal.fieldType(f.ty) + " " + idCpp.local(f.ident) + "_")
          w.wl
          val init = (f: Field) => idCpp.field(f.ident) + "(std::move(" + idCpp.local(f.ident) + "_))"
          w.wl(": " + init(r.fields.head))
          r.fields.tail.map(f => ", " + init(f)).foreach(w.wl)
          w.wl("{}")
        }

        if (r.ext.cpp) {
          w.wl
          w.wl(s"virtual ~$actualSelf() = default;")
          w.wl
          // Defining the dtor disables implicit copy/move operation generation, so re-enable them
          // Make them protected to avoid slicing
          w.wlOutdent("protected:")
          w.wl(s"$actualSelf(const $actualSelf&) = default;")
          w.wl(s"$actualSelf($actualSelf&&) = default;")
          w.wl(s"$actualSelf& operator=(const $actualSelf&) = default;")
          w.wl(s"$actualSelf& operator=($actualSelf&&) = default;")
        }
      }
      if (r.derivingTypes.contains(DerivingType.Json)) {
        w.wl
        w.wl(s"json11::Json parsed_json_from_$actualSelf (const $actualSelf& record);")
        w.wl(s"std::string  json_from_$actualSelf (const $actualSelf& record);")
        w.wl(s"$actualSelf parsed_json_to_$actualSelf (const json11::Json& json);")
        w.wl(s"$actualSelf json_to_$actualSelf (const std::string data);")
      }
    }

    writeHppFile(cppName, origin, refs.hpp, refs.hppFwds, writeCppPrototype)

    if (r.consts.nonEmpty || r.derivingTypes.nonEmpty) {
      writeCppFile(cppName, origin, refs.cpp, w => {
        generateCppConstants(w, r.consts, actualSelf)

        if (r.derivingTypes.contains(DerivingType.Eq)) {
          w.wl
          w.w(s"bool operator==(const $actualSelf& lhs, const $actualSelf& rhs)").braced {
            if(!r.fields.isEmpty) {
              writeAlignedCall(w, "return ", r.fields, " &&", "", f => s"lhs.${idCpp.field(f.ident)} == rhs.${idCpp.field(f.ident)}")
              w.wl(";")
            } else {
             w.wl("return true;")
            }
          }
          w.wl
          w.w(s"bool operator!=(const $actualSelf& lhs, const $actualSelf& rhs)").braced {
            w.wl("return !(lhs == rhs);")
          }
        }
        if (r.derivingTypes.contains(DerivingType.Ord)) {
          w.wl
          w.w(s"bool operator<(const $actualSelf& lhs, const $actualSelf& rhs)").braced {
            for(f <- r.fields) {
              w.w(s"if (lhs.${idCpp.field(f.ident)} < rhs.${idCpp.field(f.ident)})").braced {
                w.wl("return true;")
              }
              w.w(s"if (rhs.${idCpp.field(f.ident)} < lhs.${idCpp.field(f.ident)})").braced {
                w.wl("return false;")
              }
            }
            w.wl("return false;")
          }
          w.wl
          w.w(s"bool operator>(const $actualSelf& lhs, const $actualSelf& rhs)").braced {
            w.wl("return rhs < lhs;")
          }
        }
        if (r.derivingTypes.contains(DerivingType.Eq) && r.derivingTypes.contains(DerivingType.Ord)) {
          w.wl
          w.w(s"bool operator<=(const $actualSelf& lhs, const $actualSelf& rhs)").braced {
            w.wl("return !(rhs < lhs);")
          }
          w.wl
          w.w(s"bool operator>=(const $actualSelf& lhs, const $actualSelf& rhs)").braced {
            w.wl("return !(lhs < rhs);")
          }
        }

        if (r.derivingTypes.contains(DerivingType.Json)) {
          w.wl
          w.w(s"json11::Json parsed_json_from_$actualSelf (const $actualSelf& record)").braced {
            def cppFor(expr: MExpr, name: String, value: String) : String = {
              expr.base match {
                case MList => {
                  s"""std::vector<json11::Json> vector_with_json_${name};
                      |${w.getCurrentIndent()}for (auto &item : $value) {
                      |${w.getCurrentIndent()}${w.getIndent()}${cppFor(expr.args.head, "item_value", "item")}
                      |${w.getCurrentIndent()}${w.getIndent()}vector_with_json_${name}.push_back(item_value);
                      |${w.getCurrentIndent()}}
                      |${w.getCurrentIndent()}json11::Json $name = json11::Json::array({vector_with_json_${name}});""".stripMargin('|')
                }
                case MMap => {
                s"""std::map<std::string, json11::Json> map_with_json_${name};
                    |${w.getCurrentIndent()}for (auto &item : $value) {
                    |${w.getCurrentIndent()}${w.getIndent()}${cppFor(expr.args.head, "item_value", "item.second")}
                    |${w.getCurrentIndent()}${w.getIndent()}map_with_json_${name}[item.first] = item_value;
                    |${w.getCurrentIndent()}}
                    |${w.getCurrentIndent()}json11::Json $name = json11::Json(map_with_json_${name});""".stripMargin('|')
                }
                case MString => s"json11::Json $name = json11::Json($value);"
                case p: MPrimitive => p._idlName match {
                  case "bool" => s"json11::Json $name = json11::Json($value);"
                  case "i8" | "i16" | "i32" | "i64" => s"json11::Json $name = json11::Json(static_cast<int>($value));"
                  case "f32" | "f64" => s"json11::Json $name = json11::Json(static_cast<double>($value));"
                  case _ => throw new AssertionError("Unreachable")
                }
                case MDate => s"json11::Json $name = json11::Json($value.time_since_epoch().count());"
                case MOptional => s"json11::Json $name = json11::Json($value.value_or(NULL));"
                case d: MDef => d.defType match {
                  case DRecord => s"json11::Json $name = parsed_json_from_${marshal.fieldType(expr)}($value);"
                  case _ => throw new AssertionError("Unreachable")
                }
                case _ => throw new AssertionError("Unreachable")
              }
            }


            for (f <- r.fields) {
              val name = idCpp.field(f.ident)
              w.wl(cppFor(f.ty.resolved, name, s"record.$name"))
            }
            val params = r.fields.map((f) => {
              "{ \"" + idJava.method(f.ident) + "\", " + idCpp.field(f.ident) + " }"
            }).mkString(", ")
            w.wl(s"return json11::Json::object({$params});")
          }
          w.wl
          w.w(s"std::string json_from_$actualSelf (const $actualSelf& record)").braced {
            w.wl(s"return parsed_json_from_$actualSelf(record).dump();")
          }
          w.wl
          w.w(s"$actualSelf parsed_json_to_$actualSelf (const json11::Json& parsed_json)").braced {
            def cppFor(expr: MExpr, name: String, json: String) : String = {
              expr.base match {
                case MList => {
                  s"""std::vector<${marshal.fieldType(expr.args.head)}> $name;
                  |${w.getCurrentIndent()}for (auto &item : $json.array_items()) {
                  |${w.getCurrentIndent()}${w.getIndent()}${cppFor(expr.args.head, "item_value", "item")}
                  |${w.getCurrentIndent()}${w.getIndent()}$name.push_back(item_value);
                  |${w.getCurrentIndent()}}""".stripMargin('|')
                }
                case MMap => {
                  s"""std::unordered_map<std::string, ${marshal.fieldType(expr.args.head)}> $name;
                  |${w.getCurrentIndent()}for (auto &item : $json.object_items()) {
                  |${w.getCurrentIndent()}${w.getIndent()}${cppFor(expr.args.head, "item_value", "item.second")}
                  |${w.getCurrentIndent()}${w.getIndent()}$name[item.first] = item_value;
                  |${w.getCurrentIndent()}}""".stripMargin('|')
                }
                case MString => s"${marshal.typename(expr)} $name = $json.string_value();"
                case p: MPrimitive => p._idlName match {
                  case "bool" => s"${p.cName} $name = $json.bool_value();"
                  case "i8" | "i16" | "i32" | "i64" => s"${p.cName} $name = static_cast<${p.cName}>($json.int_value());"
                  case "f32" | "f64" => s"${p.cName} $name = static_cast<${p.cName}>($json.number_value());"
                  case _ => throw new AssertionError("Unreachable")
                }
                case MDate => {
                  s"""std::chrono::milliseconds {$name}_in_milliseconds(static_cast<long>($json.int_value()));
                      |${w.getCurrentIndent()}std::chrono::time_point<system_clock> $name({$name}_in_milliseconds);""".stripMargin('|')
                }
                case MOptional => cppFor(expr.args.head, name, json)
                case d: MDef => d.defType match {
                  case DRecord => s"${marshal.fieldType(expr)} $name = parsed_json_to_${marshal.fieldType(expr)}($json);"
                  case _ => throw new AssertionError("Unreachable")
                }
                case _ => throw new AssertionError("Unreachable")
              }
            }
            for (f <- r.fields) {
              w.wl(cppFor(f.ty.resolved, idCpp.field(f.ident), "parsed_json[\"" + idJava.method(f.ident) + "\"]"))
            }
            val params = r.fields.map((f) => idCpp.field(f.ident)).mkString(", ")
            w.wl(s"$actualSelf record ($params);")
            w.wl(s"return record;")
          }
          w.wl
          w.w(s"$actualSelf json_to_$actualSelf (const std::string data)").braced {
            w.wl("std::string error;")
            w.wl("json11::Json parsed_json = json11::Json::parse(data, error);")
            w.wl("if (!error.empty())").braced {
              w.wl("throw error;")
            }
            w.wl(s"return parsed_json_to_$actualSelf(parsed_json);")
          }
        }
      })
    }

  }

  override def generateInterface(origin: String, ident: Ident, doc: Doc, typeParams: Seq[TypeParam], i: Interface) {
    val refs = new CppRefs(ident.name)
    i.methods.map(m => {
      m.params.map(p => refs.find(p.ty, true))
      m.ret.foreach((x)=>refs.find(x, true))
    })
    i.consts.map(c => {
      refs.find(c.ty, true)
    })

    val self = marshal.typename(ident, i)
    val methodNamesInScope = i.methods.map(m => idCpp.method(m.ident))

    writeHppFile(ident, origin, refs.hpp, refs.hppFwds, w => {
      writeDoc(w, doc)
      writeCppTypeParams(w, typeParams)
      w.w(s"class $self").bracedSemi {
        w.wlOutdent("public:")
        // Destructor
        w.wl(s"virtual ~$self() {}")
        // Constants
        generateHppConstants(w, i.consts)
        // Methods
        for (m <- i.methods) {
          w.wl
          writeDoc(w, m.doc)
          val ret = marshal.returnType(m.ret, methodNamesInScope)
          val params = m.params.map(p => marshal.paramType(p.ty, methodNamesInScope) + " " + idCpp.local(p.ident))
          if (m.static) {
            w.wl(s"static $ret ${idCpp.method(m.ident)}${params.mkString("(", ", ", ")")};")
          } else {
            val constFlag = if (m.const) " const" else ""
            w.wl(s"virtual $ret ${idCpp.method(m.ident)}${params.mkString("(", ", ", ")")}$constFlag = 0;")
          }
        }
      }
    })

    // Cpp only generated in need of Constants
    if (i.consts.nonEmpty) {
      writeCppFile(ident, origin, refs.cpp, w => {
        generateCppConstants(w, i.consts, self)
      })
    }

  }

  def writeCppTypeParams(w: IndentWriter, params: Seq[TypeParam]) {
    if (params.isEmpty) return
    w.wl("template " + params.map(p => "typename " + idCpp.typeParam(p.ident)).mkString("<", ", ", ">"))
  }

}
