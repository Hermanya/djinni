// AUTOGENERATED FILE - DO NOT MODIFY!
// This file generated by Djinni from test.djinni

#include "NativeConflictUser.hpp"  // my header
#include "Marshal.hpp"
#include "NativeConflict.hpp"
#include "NativeLambdaInterfaceI64String.hpp"

namespace djinni_generated {

NativeConflictUser::NativeConflictUser() : ::djinni::JniInterface<::testsuite::ConflictUser, NativeConflictUser>("com/dropbox/djinni/test/ConflictUser$CppProxy") {}

NativeConflictUser::~NativeConflictUser() = default;


CJNIEXPORT void JNICALL Java_com_dropbox_djinni_test_ConflictUser_00024CppProxy_nativeDestroy(JNIEnv* jniEnv, jobject /*this*/, jlong nativeRef)
{
    try {
        DJINNI_FUNCTION_PROLOGUE1(jniEnv, nativeRef);
        delete reinterpret_cast<::djinni::CppProxyHandle<::testsuite::ConflictUser>*>(nativeRef);
    } JNI_TRANSLATE_EXCEPTIONS_RETURN(jniEnv, )
}

CJNIEXPORT jobject JNICALL Java_com_dropbox_djinni_test_ConflictUser_00024CppProxy_native_1Conflict(JNIEnv* jniEnv, jobject /*this*/, jlong nativeRef)
{
    try {
        DJINNI_FUNCTION_PROLOGUE1(jniEnv, nativeRef);
        const auto& ref = ::djinni::objectFromHandleAddress<::testsuite::ConflictUser>(nativeRef);
        auto r = ref->Conflict();
        return ::djinni::release(::djinni_generated::NativeConflict::fromCpp(jniEnv, r));
    } JNI_TRANSLATE_EXCEPTIONS_RETURN(jniEnv, 0 /* value doesn't matter */)
}

CJNIEXPORT jboolean JNICALL Java_com_dropbox_djinni_test_ConflictUser_00024CppProxy_native_1conflictArg(JNIEnv* jniEnv, jobject /*this*/, jlong nativeRef, jobject j_cs)
{
    try {
        DJINNI_FUNCTION_PROLOGUE1(jniEnv, nativeRef);
        const auto& ref = ::djinni::objectFromHandleAddress<::testsuite::ConflictUser>(nativeRef);
        auto r = ref->conflict_arg(::djinni::Set<::djinni_generated::NativeConflict>::toCpp(jniEnv, j_cs));
        return ::djinni::release(::djinni::Bool::fromCpp(jniEnv, r));
    } JNI_TRANSLATE_EXCEPTIONS_RETURN(jniEnv, 0 /* value doesn't matter */)
}

}  // namespace djinni_generated
