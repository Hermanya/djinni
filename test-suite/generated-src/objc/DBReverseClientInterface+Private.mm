// AUTOGENERATED FILE - DO NOT MODIFY!
// This file generated by Djinni from client_interface.djinni

#import "DBReverseClientInterface+Private.h"
#import "DBReverseClientInterface.h"
#import "DBClientReturnedRecord+Private.h"
#import "DJICppWrapperCache+Private.h"
#import "DJIError.h"
#import "DJIMarshal+Private.h"
#include <exception>
#include <stdexcept>
#include <utility>

static_assert(__has_feature(objc_arc), "Djinni requires ARC to be enabled for this file");

@interface DBReverseClientInterface ()

- (id)initWithCpp:(const std::shared_ptr<::testsuite::ReverseClientInterface>&)cppRef;

@end

@implementation DBReverseClientInterface {
    ::djinni::CppProxyCache::Handle<std::shared_ptr<::testsuite::ReverseClientInterface>> _cppRefHandle;
}

- (id)initWithCpp:(const std::shared_ptr<::testsuite::ReverseClientInterface>&)cppRef
{
    if (self = [super init]) {
        _cppRefHandle.assign(cppRef);
    }
    return self;
}

- (nonnull NSString *)returnStr {
    try {
        auto objcpp_result_ = _cppRefHandle.get()->return_str();
        return ::djinni::String::fromCpp(objcpp_result_);
    } DJINNI_TRANSLATE_EXCEPTIONS()
}

- (nonnull NSString *)methTakingInterface:(nullable DBReverseClientInterface *)i {
    try {
        auto objcpp_result_ = _cppRefHandle.get()->meth_taking_interface(::djinni_generated::ReverseClientInterface::toCpp(i));
        return ::djinni::String::fromCpp(objcpp_result_);
    } DJINNI_TRANSLATE_EXCEPTIONS()
}

- (nonnull NSString *)methTakingOptionalInterface:(nullable DBReverseClientInterface *)i {
    try {
        auto objcpp_result_ = _cppRefHandle.get()->meth_taking_optional_interface(::djinni::Optional<std::experimental::optional, ::djinni_generated::ReverseClientInterface>::toCpp(i));
        return ::djinni::String::fromCpp(objcpp_result_);
    } DJINNI_TRANSLATE_EXCEPTIONS()
}

+ (nullable DBReverseClientInterface *)create {
    try {
        auto objcpp_result_ = ::testsuite::ReverseClientInterface::create();
        return ::djinni_generated::ReverseClientInterface::fromCpp(objcpp_result_);
    } DJINNI_TRANSLATE_EXCEPTIONS()
}

- (void)takeLambdaWhichReturnsNothing:(nonnull void  (^) (int64_t  arg))cb {
    try {
        _cppRefHandle.get()->take_lambda_which_returns_nothing([=](int64_t param_0) { cb(::djinni::I64::fromCpp(param_0));});
    } DJINNI_TRANSLATE_EXCEPTIONS()
}

- (void)takeLambdaWhichReturnsString:(nonnull NSString * _Nonnull (^) (int64_t  arg))cb {
    try {
        _cppRefHandle.get()->take_lambda_which_returns_string([=](int64_t param_0) -> std::string { return ::djinni::String::toCpp(cb(::djinni::I64::fromCpp(param_0)));});
    } DJINNI_TRANSLATE_EXCEPTIONS()
}

- (void)takeBinaryLambdaWhichReturnsNothing:(nonnull void  (^) (int64_t  arg, NSString * _Nonnull arg1))cb {
    try {
        _cppRefHandle.get()->take_binary_lambda_which_returns_nothing([=](int64_t param_0,std::string param_1) { cb(::djinni::I64::fromCpp(param_0),::djinni::String::fromCpp(param_1));});
    } DJINNI_TRANSLATE_EXCEPTIONS()
}

- (void)getRecord:(int64_t)recordId
completionCallback:(nonnull void  (^) (DBClientReturnedRecord * _Nonnull clientReturnedRecord))completionCallback {
    try {
        _cppRefHandle.get()->get_record(::djinni::I64::toCpp(recordId),
                                        [=](::testsuite::ClientReturnedRecord param_0) { completionCallback(::djinni_generated::ClientReturnedRecord::fromCpp(param_0));});
    } DJINNI_TRANSLATE_EXCEPTIONS()
}

- (void)testTypedClasses:(int64_t)recordId
      completionCallback:(nonnull void  (^) (DBClientReturnedRecord * _Nullable clientReturnedRecord, NSDictionary<NSString *, NSString *> * _Nonnull arg))completionCallback {
    try {
        _cppRefHandle.get()->test_typed_classes(::djinni::I64::toCpp(recordId),
                                                [=](std::experimental::optional<::testsuite::ClientReturnedRecord> param_0,std::unordered_map<std::string, std::string> param_1) { completionCallback(::djinni::Optional<std::experimental::optional, ::djinni_generated::ClientReturnedRecord>::fromCpp(param_0),::djinni::Map<::djinni::String, ::djinni::String>::fromCpp(param_1));});
    } DJINNI_TRANSLATE_EXCEPTIONS()
}

namespace djinni_generated {

auto ReverseClientInterface::toCpp(ObjcType objc) -> CppType
{
    if (!objc) {
        return nullptr;
    }
    return objc->_cppRefHandle.get();
}

auto ReverseClientInterface::fromCppOpt(const CppOptType& cpp) -> ObjcType
{
    if (!cpp) {
        return nil;
    }
    return ::djinni::get_cpp_proxy<DBReverseClientInterface>(cpp);
}

}  // namespace djinni_generated

@end
