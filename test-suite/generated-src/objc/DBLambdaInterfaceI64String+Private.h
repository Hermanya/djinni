// AUTOGENERATED FILE - DO NOT MODIFY!
// This file generated by Djinni from 

#include "lambda_interface_i64_string.hpp"
#include <memory>

static_assert(__has_feature(objc_arc), "Djinni requires ARC to be enabled for this file");

@class DBLambdaInterfaceI64String;

namespace djinni_generated {

class LambdaInterfaceI64String
{
public:
    using CppType = std::shared_ptr<::testsuite::LambdaInterfaceI64String>;
    using CppOptType = std::shared_ptr<::testsuite::LambdaInterfaceI64String>;
    using ObjcType = DBLambdaInterfaceI64String*;

    using Boxed = LambdaInterfaceI64String;

    static CppType toCpp(ObjcType objc);
    static ObjcType fromCppOpt(const CppOptType& cpp);
    static ObjcType fromCpp(const CppType& cpp) { return fromCppOpt(cpp); }

private:
    class ObjcProxy;
};

}  // namespace djinni_generated

