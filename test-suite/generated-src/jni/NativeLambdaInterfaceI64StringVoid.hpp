// AUTOGENERATED FILE - DO NOT MODIFY!
// This file generated by Djinni from 

#pragma once

#include "djinni_support.hpp"
#include "lambda_interface_i64_string_void.hpp"

namespace djinni_generated {

class NativeLambdaInterfaceI64StringVoid final : ::djinni::JniInterface<::testsuite::LambdaInterfaceI64StringVoid, NativeLambdaInterfaceI64StringVoid> {
public:
    using CppType = std::shared_ptr<::testsuite::LambdaInterfaceI64StringVoid>;
    using CppOptType = std::shared_ptr<::testsuite::LambdaInterfaceI64StringVoid>;
    using JniType = jobject;

    using Boxed = NativeLambdaInterfaceI64StringVoid;

    ~NativeLambdaInterfaceI64StringVoid();

    static CppType toCpp(JNIEnv* jniEnv, JniType j) { return ::djinni::JniClass<NativeLambdaInterfaceI64StringVoid>::get()._fromJava(jniEnv, j); }
    static ::djinni::LocalRef<JniType> fromCppOpt(JNIEnv* jniEnv, const CppOptType& c) { return {jniEnv, ::djinni::JniClass<NativeLambdaInterfaceI64StringVoid>::get()._toJava(jniEnv, c)}; }
    static ::djinni::LocalRef<JniType> fromCpp(JNIEnv* jniEnv, const CppType& c) { return fromCppOpt(jniEnv, c); }

private:
    NativeLambdaInterfaceI64StringVoid();
    friend ::djinni::JniClass<NativeLambdaInterfaceI64StringVoid>;
    friend ::djinni::JniInterface<::testsuite::LambdaInterfaceI64StringVoid, NativeLambdaInterfaceI64StringVoid>;

    class JavaProxy final : ::djinni::JavaProxyHandle<JavaProxy>, public ::testsuite::LambdaInterfaceI64StringVoid
    {
    public:
        JavaProxy(JniType j);
        ~JavaProxy();

        void run(int64_t param_1, const std::string & param_2) override;

    private:
        friend ::djinni::JniInterface<::testsuite::LambdaInterfaceI64StringVoid, ::djinni_generated::NativeLambdaInterfaceI64StringVoid>;
    };

    const ::djinni::GlobalRef<jclass> clazz { ::djinni::jniFindClass("com/dropbox/djinni/test/LambdaInterfaceI64StringVoid") };
    const jmethodID method_run { ::djinni::jniGetMethodID(clazz.get(), "run", "(JLjava/lang/String;)V") };
};

}  // namespace djinni_generated