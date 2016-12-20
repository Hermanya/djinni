#include "reverse_client_interface_impl.hpp"
#include <iostream>

namespace testsuite {

std::string ReverseClientInterfaceImpl::return_str() const {
    return "test";
}

std::string ReverseClientInterfaceImpl::meth_taking_interface(const std::shared_ptr<ReverseClientInterface> & i) {
    if (i) {
        return i->return_str();
    } else {
        return {};
    }
}

std::string ReverseClientInterfaceImpl::meth_taking_optional_interface(const std::shared_ptr<ReverseClientInterface> & i) {
    if (i) {
        return i->return_str();
    } else {
        return {};
    }
}

void ReverseClientInterfaceImpl::take_lambda_which_returns_nothing(std::function<void(int64_t)> cb) {
    cb(123);
};

void ReverseClientInterfaceImpl::take_lambda_which_returns_string(std::function<std::string(int64_t)> cb) {
    cb(123);
};

void ReverseClientInterfaceImpl::take_binary_lambda_which_returns_nothing(std::function<void(int64_t, std::string)> cb) {
    cb(123, "omg");
};

void ReverseClientInterfaceImpl::get_record(int64_t record_id, const std::function<void(ClientReturnedRecord)> & completionCallback) {
    std::experimental::optional<std::string> maybe_something_else;
    testsuite::ClientReturnedRecord record(record_id, "test string", maybe_something_else);
    completionCallback(record);
};

void ReverseClientInterfaceImpl::test_typed_classes(int64_t record_id, const std::function<void(std::experimental::optional<::testsuite::ClientReturnedRecord>,std::unordered_map<std::string, std::string>)> & completionCallback) {
    std::experimental::optional<std::string> maybe_something_else;
    testsuite::ClientReturnedRecord record(record_id, "test string", maybe_something_else);
    std::experimental::optional<testsuite::ClientReturnedRecord> the_record = record;
    std::unordered_map<std::string, std::string> map;
    completionCallback(the_record, map);
};


std::shared_ptr<ReverseClientInterface> ReverseClientInterface::create() {
    return std::make_shared<ReverseClientInterfaceImpl>();
}

} // namespace testsuite
