#include <iostream>
#include <CLI11/CLI11.hpp>
#include <nlohmann/json.hpp>
#include <proxybroker/services/collector.hpp>
#include <proxybroker/cmds/find.hpp>

void find(SubcommandFindOptions const& opt){
  nlohmann::json proxySources = Collector::getSources();
  nlohmann::json proxies = Collector::getProxies(proxySources);
  // std::cout << proxySources << std::endl;
}