#include "client_returned_record.hpp"
#include "reverse_client_interface.hpp"

namespace testsuite {

class ReverseClientInterfaceImpl : public ReverseClientInterface {
    public:
    ReverseClientInterfaceImpl() {}
    virtual ~ReverseClientInterfaceImpl() {}

    virtual std::string return_str() const override;

    virtual std::string meth_taking_interface(const std::shared_ptr<ReverseClientInterface> & i) override;

    virtual std::string meth_taking_optional_interface(const std::shared_ptr<ReverseClientInterface> & i) override;

    virtual void take_lambda_which_returns_nothing(std::function<void(int64_t)> cb) override;
    virtual void take_lambda_which_returns_string(std::function<std::string(int64_t)> cb) override;
    virtual void take_binary_lambda_which_returns_nothing(std::function<void(int64_t, std::string)> cb) override;
    virtual void get_record(int64_t record_id, const std::function<void(ClientReturnedRecord)> & completionCallback) override;
    static std::shared_ptr<ReverseClientInterface> create();
};

} // namespace testsuite
