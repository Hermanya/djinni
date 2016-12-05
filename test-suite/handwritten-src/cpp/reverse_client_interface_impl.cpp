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

std::shared_ptr<ReverseClientInterface> ReverseClientInterface::create() {
    return std::make_shared<ReverseClientInterfaceImpl>();
}

} // namespace testsuite
