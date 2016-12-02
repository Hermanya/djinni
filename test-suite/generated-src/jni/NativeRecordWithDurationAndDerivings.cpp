// AUTOGENERATED FILE - DO NOT MODIFY!
// This file generated by Djinni from duration.djinni

#include "NativeRecordWithDurationAndDerivings.hpp"  // my header
#include "Duration-jni.hpp"
#include "Marshal.hpp"
#include "NativeLambdaInterfaceI64String.hpp"

namespace djinni_generated {

NativeRecordWithDurationAndDerivings::NativeRecordWithDurationAndDerivings() = default;

NativeRecordWithDurationAndDerivings::~NativeRecordWithDurationAndDerivings() = default;

auto NativeRecordWithDurationAndDerivings::fromCpp(JNIEnv* jniEnv, const CppType& c) -> ::djinni::LocalRef<JniType> {
    const auto& data = ::djinni::JniClass<NativeRecordWithDurationAndDerivings>::get();
    auto r = ::djinni::LocalRef<JniType>{jniEnv->NewObject(data.clazz.get(), data.jconstructor,
                                                           ::djinni::get(::djinni::Duration<::djinni::F64, ::djinni::Duration_ns>::fromCpp(jniEnv, c.dt)))};
    ::djinni::jniExceptionCheck(jniEnv);
    return r;
}

auto NativeRecordWithDurationAndDerivings::toCpp(JNIEnv* jniEnv, JniType j) -> CppType {
    ::djinni::JniLocalScope jscope(jniEnv, 2);
    assert(j != nullptr);
    const auto& data = ::djinni::JniClass<NativeRecordWithDurationAndDerivings>::get();
    return {::djinni::Duration<::djinni::F64, ::djinni::Duration_ns>::toCpp(jniEnv, jniEnv->GetObjectField(j, data.field_mDt))};
}

}  // namespace djinni_generated
