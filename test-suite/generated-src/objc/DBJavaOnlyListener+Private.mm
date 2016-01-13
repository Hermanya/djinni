// AUTOGENERATED FILE - DO NOT MODIFY!
// This file generated by Djinni from listeners.djinni

#import "DBJavaOnlyListener.h"

static_assert(__has_feature(objc_arc), "Djinni requires ARC to be enabled for this file");

namespace djinni_generated {

auto JavaOnlyListener::toCpp(ObjcType objc) -> CppType
{
    if (!objc) {
        return nullptr;
    }
    return ::djinni::get_objc_proxy<ObjcProxy>(objc);
}

auto JavaOnlyListener::fromCppOpt(const CppOptType& cpp) -> ObjcType
{
    if (!cpp) {
        return nil;
    }
    return ::djinni::get_cpp_proxy<DBJavaOnlyListener>(cpp);
}

}  // namespace djinni_generated
