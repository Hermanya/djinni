// AUTOGENERATED FILE - DO NOT MODIFY!
// This file generated by Djinni from test.djinni

#include "NativeConflict.hpp"  // my header
#include "NativeLambdaInterfaceI64String.hpp"

namespace djinni_generated {

NativeConflict::NativeConflict() : ::djinni::JniInterface<::testsuite::Conflict, NativeConflict>("com/dropbox/djinni/test/Conflict$CppProxy") {}

NativeConflict::~NativeConflict() = default;


CJNIEXPORT void JNICALL Java_com_dropbox_djinni_test_Conflict_00024CppProxy_nativeDestroy(JNIEnv* jniEnv, jobject /*this*/, jlong nativeRef)
{
    try {
        DJINNI_FUNCTION_PROLOGUE1(jniEnv, nativeRef);
        delete reinterpret_cast<::djinni::CppProxyHandle<::testsuite::Conflict>*>(nativeRef);
    } JNI_TRANSLATE_EXCEPTIONS_RETURN(jniEnv, )
}

}  // namespace djinni_generated
