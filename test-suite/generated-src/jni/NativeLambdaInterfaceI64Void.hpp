// AUTOGENERATED FILE - DO NOT MODIFY!
// This file generated by Djinni from 

#pragma once

#include "djinni_support.hpp"
#include "lambda_interface_i64_void.hpp"

namespace djinni_generated {

class NativeLambdaInterfaceI64Void final : ::djinni::JniInterface<::testsuite::LambdaInterfaceI64Void, NativeLambdaInterfaceI64Void> {
public:
    using CppType = std::shared_ptr<::testsuite::LambdaInterfaceI64Void>;
    using CppOptType = std::shared_ptr<::testsuite::LambdaInterfaceI64Void>;
    using JniType = jobject;

    using Boxed = NativeLambdaInterfaceI64Void;

    ~NativeLambdaInterfaceI64Void();

    static CppType toCpp(JNIEnv* jniEnv, JniType j) { return ::djinni::JniClass<NativeLambdaInterfaceI64Void>::get()._fromJava(jniEnv, j); }
    static ::djinni::LocalRef<JniType> fromCppOpt(JNIEnv* jniEnv, const CppOptType& c) { return {jniEnv, ::djinni::JniClass<NativeLambdaInterfaceI64Void>::get()._toJava(jniEnv, c)}; }
    static ::djinni::LocalRef<JniType> fromCpp(JNIEnv* jniEnv, const CppType& c) { return fromCppOpt(jniEnv, c); }

private:
    NativeLambdaInterfaceI64Void();
    friend ::djinni::JniClass<NativeLambdaInterfaceI64Void>;
    friend ::djinni::JniInterface<::testsuite::LambdaInterfaceI64Void, NativeLambdaInterfaceI64Void>;

    class JavaProxy final : ::djinni::JavaProxyHandle<JavaProxy>, public ::testsuite::LambdaInterfaceI64Void
    {
    public:
        JavaProxy(JniType j);
        ~JavaProxy();

        void run(int64_t param_1) override;

    private:
        friend ::djinni::JniInterface<::testsuite::LambdaInterfaceI64Void, ::djinni_generated::NativeLambdaInterfaceI64Void>;
    };

    const ::djinni::GlobalRef<jclass> clazz { ::djinni::jniFindClass("com/dropbox/djinni/test/LambdaInterfaceI64Void") };
    const jmethodID method_run { ::djinni::jniGetMethodID(clazz.get(), "run", "(J)V") };
};

}  // namespace djinni_generated
