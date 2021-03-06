// AUTOGENERATED FILE - DO NOT MODIFY!
// This file generated by Djinni from 

#include "NativeLambdaInterfaceClientReturnedRecordVoid.hpp"  // my header
#include "NativeClientReturnedRecord.hpp"

namespace djinni_generated {

NativeLambdaInterfaceClientReturnedRecordVoid::NativeLambdaInterfaceClientReturnedRecordVoid() : ::djinni::JniInterface<::testsuite::LambdaInterfaceClientReturnedRecordVoid, NativeLambdaInterfaceClientReturnedRecordVoid>() {}

NativeLambdaInterfaceClientReturnedRecordVoid::~NativeLambdaInterfaceClientReturnedRecordVoid() = default;

NativeLambdaInterfaceClientReturnedRecordVoid::JavaProxy::JavaProxy(JniType j) : Handle(::djinni::jniGetThreadEnv(), j) { }

NativeLambdaInterfaceClientReturnedRecordVoid::JavaProxy::~JavaProxy() = default;

void NativeLambdaInterfaceClientReturnedRecordVoid::JavaProxy::run(const ::testsuite::ClientReturnedRecord & c_param_1) {
    auto jniEnv = ::djinni::jniGetThreadEnv();
    ::djinni::JniLocalScope jscope(jniEnv, 10);
    const auto& data = ::djinni::JniClass<::djinni_generated::NativeLambdaInterfaceClientReturnedRecordVoid>::get();
    jniEnv->CallVoidMethod(Handle::get().get(), data.method_run,
                           ::djinni::get(::djinni_generated::NativeClientReturnedRecord::fromCpp(jniEnv, c_param_1)));
    ::djinni::jniExceptionCheck(jniEnv);
}

}  // namespace djinni_generated
