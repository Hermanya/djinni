package djinni

import djinni.ast._
import djinni.generatorTools._
import djinni.meta._

class JavaMarshal(spec: Spec) extends Marshal(spec) {

  val javaNullableAnnotation = spec.javaNullableAnnotation.map(pkg => '@' + pkg.split("\\.").last)
  val javaNonnullAnnotation = spec.javaNonnullAnnotation.map(pkg => '@' + pkg.split("\\.").last)

  override def typename(tm: MExpr): String = toJavaType(tm, None)
  def typename(name: String, ty: TypeDef): String = idJava.ty(name)

  override def fqTypename(tm: MExpr): String = toJavaType(tm, spec.javaPackage)
  def fqTypename(name: String, ty: TypeDef): String = withPackage(spec.javaPackage, idJava.ty(name))

  override def paramType(tm: MExpr): String = typename(tm)
  override def fqParamType(tm: MExpr): String = fqTypename(tm)

  override def returnType(ret: Option[TypeRef]): String = ret.fold("void")(ty => toJavaType(ty.resolved, None))
  override def fqReturnType(ret: Option[TypeRef]): String = ret.fold("void")(ty => toJavaType(ty.resolved, spec.javaPackage))

  override def fieldType(tm: MExpr): String = typename(tm)
  override def fqFieldType(tm: MExpr): String = fqTypename(tm)

  override def toCpp(tm: MExpr, expr: String, cppMarshal: CppMarshal): String = throw new AssertionError("direct java to cpp conversion not possible")
  override def fromCpp(tm: MExpr, expr: String): String = throw new AssertionError("direct cpp to java conversion not possible")

  def references(m: Meta): Seq[SymbolReference] = m match {
    case o: MOpaque =>
      o match {
        case MNullaryLambda | MUnaryLambda | MBinaryLambda => List(ImportRef("java.util.function.*"))
        case MList => List(ImportRef("java.util.ArrayList"))
        case MSet => List(ImportRef("java.util.HashSet"))
        case MMap => List(ImportRef("java.util.HashMap"))
        case MDate => List(ImportRef("java.util.Date"))
        case _ => List()
      }
    case _ => List()
  }

  val interfaceNullityAnnotation = if (spec.cppNnType.nonEmpty) javaNonnullAnnotation else javaNullableAnnotation

  def nullityAnnotation(ty: Option[TypeRef]): Option[String] = ty.map(nullityAnnotation).getOrElse(None)
  def nullityAnnotation(ty: TypeRef): Option[String] = {
    ty.resolved.base match {
      case MOptional => javaNullableAnnotation
      case p: MPrimitive => None
      case m: MDef => m.defType match {
          case DInterface => interfaceNullityAnnotation
          case DEnum => javaNonnullAnnotation
          case DRecord => javaNonnullAnnotation
        }
      case e: MExtern => e.defType match {
        case DInterface => interfaceNullityAnnotation
        case DRecord => if(e.java.reference) javaNonnullAnnotation else None
        case DEnum => javaNonnullAnnotation
      }
      case _ => javaNonnullAnnotation
    }
  }

  def isReference(td: TypeDecl) = td.body match {
    case i: Interface => true
    case r: Record => true
    case e: Enum =>  true
  }

  private def toJavaType(tm: MExpr, packageName: Option[String]): String = {
    def args(tm: MExpr) = if (tm.args.isEmpty) "" else tm.args.map(f(_, true)).filter(t => t != "Void").mkString("<", ", ", ">")
    def f(tm: MExpr, needRef: Boolean): String = {
      tm.base match {
        case MOptional =>
          // HACK: We use "null" for the empty optional in Java.
          assert(tm.args.size == 1)
          val arg = tm.args.head
          arg.base match {
            case p: MPrimitive => p.jBoxed
            case MOptional => throw new AssertionError("nested optional?")
            case m => f(arg, true)
          }
        case e: MExtern => (if(needRef) e.java.boxed else e.java.typename) + (if(e.java.generic) args(tm) else "")
        case o =>
          val base = o match {
            case p: MPrimitive => if (needRef) p.jBoxed else p.jName
            case MString => "String"
            case MDate => "Date"
            case MBinary => "byte[]"
            case MOptional => throw new AssertionError("optional should have been special cased")
            case MNullaryLambda | MUnaryLambda | MBinaryLambda => {
              val args = tm.args.dropRight(1)
              def lambda_that_returns () : String = {
                args.size match {
                  case 0 => "Supplier"
                  case 1 => "Function"
                  case 2 => "BiFunction"
                  case _ => throw new AssertionError(s"Java does not support lambdas with that ${args.size} of args")
                }
              }
              tm.args.takeRight(1).head.base match {
                case p: MPrimitive => p.idlName match {
                  case "void" => args.size match {
                    case 1 => "Consumer"
                    case 2 => "BiConsumer"
                    case _ => throw new AssertionError(s"Java does not support lambdas with that ${args.size} of args")
                  }
                  case _ => lambda_that_returns()
                }
                case _ => lambda_that_returns()
              }
            }
            case MList => "ArrayList"
            case MSet => "HashSet"
            case MMap => "HashMap"
            case d: MDef => withPackage(packageName, idJava.ty(d.name))
            case e: MExtern => throw new AssertionError("unreachable")
            case p: MParam => idJava.typeParam(p.name)
          }
          base + args(tm)
      }
    }
    f(tm, false)
  }

  private def withPackage(packageName: Option[String], t: String) = packageName.fold(t)(_ + "." + t)

 }
