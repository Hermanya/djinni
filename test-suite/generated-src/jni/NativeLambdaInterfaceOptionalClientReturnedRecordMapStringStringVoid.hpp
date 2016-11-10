// AUTOGENERATED FILE - DO NOT MODIFY!
// This file generated by Djinni from 

#pragma once

#include "djinni_support.hpp"
#include "lambda_interface_optional_client_returned_record_map_string_string_void.hpp"

namespace djinni_generated {

class NativeLambdaInterfaceOptionalClientReturnedRecordMapStringStringVoid final : ::djinni::JniInterface<::testsuite::LambdaInterfaceOptionalClientReturnedRecordMapStringStringVoid, NativeLambdaInterfaceOptionalClientReturnedRecordMapStringStringVoid> {
public:
    using CppType = std::shared_ptr<::testsuite::LambdaInterfaceOptionalClientReturnedRecordMapStringStringVoid>;
    using CppOptType = std::shared_ptr<::testsuite::LambdaInterfaceOptionalClientReturnedRecordMapStringStringVoid>;
    using JniType = jobject;

    using Boxed = NativeLambdaInterfaceOptionalClientReturnedRecordMapStringStringVoid;

    ~NativeLambdaInterfaceOptionalClientReturnedRecordMapStringStringVoid();

    static CppType toCpp(JNIEnv* jniEnv, JniType j) { return ::djinni::JniClass<NativeLambdaInterfaceOptionalClientReturnedRecordMapStringStringVoid>::get()._fromJava(jniEnv, j); }
    static ::djinni::LocalRef<JniType> fromCppOpt(JNIEnv* jniEnv, const CppOptType& c) { return {jniEnv, ::djinni::JniClass<NativeLambdaInterfaceOptionalClientReturnedRecordMapStringStringVoid>::get()._toJava(jniEnv, c)}; }
    static ::djinni::LocalRef<JniType> fromCpp(JNIEnv* jniEnv, const CppType& c) { return fromCppOpt(jniEnv, c); }

private:
    NativeLambdaInterfaceOptionalClientReturnedRecordMapStringStringVoid();
    friend ::djinni::JniClass<NativeLambdaInterfaceOptionalClientReturnedRecordMapStringStringVoid>;
    friend ::djinni::JniInterface<::testsuite::LambdaInterfaceOptionalClientReturnedRecordMapStringStringVoid, NativeLambdaInterfaceOptionalClientReturnedRecordMapStringStringVoid>;

    class JavaProxy final : ::djinni::JavaProxyHandle<JavaProxy>, public ::testsuite::LambdaInterfaceOptionalClientReturnedRecordMapStringStringVoid
    {
    public:
        JavaProxy(JniType j);
        ~JavaProxy();

        void run(const std::experimental::optional<::testsuite::ClientReturnedRecord> & param_1, const std::unordered_map<std::string, std::string> & param_2) override;

    private:
        friend ::djinni::JniInterface<::testsuite::LambdaInterfaceOptionalClientReturnedRecordMapStringStringVoid, ::djinni_generated::NativeLambdaInterfaceOptionalClientReturnedRecordMapStringStringVoid>;
    };

    const ::djinni::GlobalRef<jclass> clazz { ::djinni::jniFindClass("com/dropbox/djinni/test/LambdaInterfaceOptionalClientReturnedRecordMapStringStringVoid") };
    const jmethodID method_run { ::djinni::jniGetMethodID(clazz.get(), "run", "(Lcom/dropbox/djinni/test/ClientReturnedRecord;Ljava/util/HashMap;)V") };
};

}  // namespace djinni_generated