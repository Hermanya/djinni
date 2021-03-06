package com.dropbox.djinni.test;

import junit.framework.TestCase;

public class ClientInterfaceTest extends TestCase {

    private ClientInterface jClientInterface;

    @Override
    protected void setUp() {
        jClientInterface = new ClientInterfaceImpl();
    }

    public void testClientReturn() {
        TestHelpers.checkClientInterfaceAscii(jClientInterface);
    }

    public void testClientReturnUTF8() {
        TestHelpers.checkClientInterfaceNonascii(jClientInterface);
    }

    public void testClientInterfaceArgs() {
        TestHelpers.checkClientInterfaceArgs(jClientInterface);
    }

    public void testReverseClientInterfaceArgs() {
        ReverseClientInterface i = ReverseClientInterface.create();

        assertEquals(i.methTakingInterface(i), "test");
        assertEquals(i.methTakingOptionalInterface(i), "test");

        i.takeLambdaWhichReturnsNothing(x -> {
            assertEquals(x.longValue(), 123);
        });
        i.takeLambdaWhichReturnsString(x -> {
            assertEquals(x.longValue(), 123);
            return "test";
        });
        i.takeBinaryLambdaWhichReturnsNothing((x, y) -> {
            assertEquals(x.longValue(), 123);
            assertEquals(y, "omg");
        });
        i.getRecord(123, (record) -> {
            assertEquals(record.getRecordId(), 123);
            assertEquals(record.getContent(), "test string");
            assertEquals(record.getMisc(), null);
        });
        i.testTypedClasses(123, (record, map) -> {
            assertEquals(record.getRecordId(), 123);
            assertEquals(record.getContent(), "test string");
            assertEquals(record.getMisc(), null);
        });
    }
}
