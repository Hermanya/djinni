#import "DBClientInterfaceImpl.h"
#import "DBClientInterface+Private.h"
#import "DBReverseClientInterface.h"
#import "DBTestHelpers.h"
#import <XCTest/XCTest.h>

using namespace testsuite;

@interface DBClientInterfaceTests : XCTestCase

@end

@implementation DBClientInterfaceTests

- (void)setUp
{
    [super setUp];
}

- (void)tearDown
{
    [super tearDown];
}

- (void)testCppString
{
    [DBTestHelpers checkClientInterfaceAscii:[[DBClientInterfaceImpl alloc] init]];
}

- (void)testCppUTF8String
{
    [DBTestHelpers checkClientInterfaceNonascii:[[DBClientInterfaceImpl alloc] init]];
}

- (void)testClientInterfaceArgs
{
    [DBTestHelpers checkClientInterfaceArgs:[[DBClientInterfaceImpl alloc] init]];
}

- (void)testReverseClientInterfaceArgs
{
    DBReverseClientInterface * i = [DBReverseClientInterface create];

    XCTAssertEqualObjects([i methTakingInterface:i], @"test");
    XCTAssertEqualObjects([i methTakingOptionalInterface:i], @"test");

    [i takeLambdaWhichReturnsNothing:^(int64_t named_param_0) {
        XCTAssertEqual(named_param_0, 123);
    }];

    [i takeLambdaWhichReturnsString:^NSString * _Nonnull(int64_t named_param_0) {
        XCTAssertEqual(named_param_0, 123);
        return @"test";
    }];

    [i takeBinaryLambdaWhichReturnsNothing:^(int64_t user_id, NSString * _Nonnull named_param_1) {
        XCTAssertEqual(user_id, 123);
        XCTAssertEqualObjects(named_param_1, @"omg");
    }];

    [i getRecord:123 completionCallback:^(DBClientReturnedRecord * _Nonnull record) {
        XCTAssertEqual(record.recordId, 123);
        XCTAssertEqualObjects(record.content, @"test string");
        XCTAssertEqualObjects(record.misc, nil);
    }];
}

- (void)testObjcInterfaceWrapper
{
    __weak id <DBClientInterface> objcClientInterfaceWeak;
    @autoreleasepool {
        std::shared_ptr<ClientInterface> cppClientInterface1, cppClientInterface2;
        @autoreleasepool {
            id <DBClientInterface> objcClientInterface = [[DBClientInterfaceImpl alloc] init];
            objcClientInterfaceWeak = objcClientInterface;
            cppClientInterface1 = ::djinni_generated::ClientInterface::toCpp(objcClientInterface);
            cppClientInterface2 = ::djinni_generated::ClientInterface::toCpp(objcClientInterface);
            XCTAssertEqual(cppClientInterface1, cppClientInterface2);
        }
        XCTAssertNotNil(objcClientInterfaceWeak);
        cppClientInterface1.reset();
        cppClientInterface2.reset();
    }
    XCTAssertNil(objcClientInterfaceWeak);
}

@end
