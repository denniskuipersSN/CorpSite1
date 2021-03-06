import groovy.json.JsonSlurper

@NonCPS
def parseJsonText(String json) {
  def object = new JsonSlurper().parseText(json)
  if(object instanceof groovy.json.internal.LazyMap) {
      return new HashMap<>(object)
  }
  return object
}

pipeline {
    agent any
    parameters {
        string(name: 'Artifact Location', defaultValue: '', description: 'Artifact to deploy on infrastructure')
		string(name: 'URL', defaultValue: 'https://dennisdemoinstancedemo.service-now.com', description: 'Servicenow Instance URL')
        string(name: 'UserName', defaultValue: 'admin', description: 'User Name of SNOW Instance')
        password(name: 'Password', defaultValue: 'Sh0wc@se', description: 'Password of SNOW Instance')
    }
    stages {

		stage('Infrastructure Provision') {
            steps {
                snDevOpsStep(enabled:true,ignoreErrors:true)
                println 'Provision Infrastructure using TFE'
				script {
					try {
						println "Provisioning Catalog"
						def creds = "${params.UserName}:${params.Password}"
						println 'creds:'+creds
						def auth = "Basic " + creds.bytes.encodeBase64().toString();
						def baseURL = "${params.URL}"

						def reqItem;
						//parser = new JsonSlurper()
						println 'About to order catalog'
						def url_snow = baseURL + '/api/now/cmp_catalog_api/submitrequest?cat_id=0d4d0f40dbbb1410a091ea42ca9619cb';
						println 'order catalog url:'+url_snow
						def post = new URL(url_snow).openConnection();

						def buildNumber = "${BUILD_NUMBER}"
						//def orderFormData = '{"CloudAccount":"'+cloudAccountName+'","ScheduleProfile":"-- No Schedule --","UserGroup":"cfcbad03d711110050f5edcb9e61038f","ScheduleTimeZone":"America/Los_Angeles","BusinessService":"","'+catalogName+'_demoinfra_prefix":"'+catalogName+'","CostCenter":"","CreateResourceGroup":"Yes","Application":"","StackName":"'+stackName+'","Location":"Azure Datacenter - westus","ResourceGroupName":"'+catalogName+'","'+catalogName+'_simplevm_rgName":"1118957fdb1450103a645635dc9619fd","'+catalogName+'_simplevm_hradwareType":"69281db7db181010c7c4d8c75e961995","'+catalogName+'_simplevm_network":"97b8d1c0dbac50103a645635dc961976","'+catalogName+'_simplevm_subnet":"9fb8d1c0dbac50103a645635dc961976","'+catalogName+'_simplevm_vmname":"'+vmName+'"}';
						//'{"CloudAccount": "AzureCA", "ScheduleProfile": "-- No Schedule --", "UserGroup": "cfcbad03d711110050f5edcb9e61038f", "ScheduleTimeZone": "America/Los_Angeles", "BusinessService": "", "'+catalogName+'_demoinfra_prefix": "'+catalogName+'", "CostCenter": "", "CreateResourceGroup": "Yes", "Application": "", "StackName": "'+stackName+'", "Location": "Azure Datacenter - westus", "ResourceGroupName": "'+catalogName+'"}'
						//def orderFormData = '{"CreateResourceGroup":true,"CloudAccount":"'+cloudAccountName+'","'+catalogName+'_simplevm_network":"Seattle_dev_network-DND","LeaseEndDate":"2020-07-27 11:47:58","ScheduleProfile":"-- No Schedule --","UserGroup":"cfcbad03d711110050f5edcb9e61038f","'+catalogName+'_simplevm_hradwareType":"Standard_B1ls","ScheduleTimeZone":"America/Los_Angeles","BusinessService":"","SubscriptionId":"2f704d46-a185-40db-9a50-08c6f04e4af7","CostCenter":"","'+catalogName+'_simplevm_vmname":"TestVmName","Application":"","ResourceGroup":"mattest1","StackName":"'+stackName+'","Location":"Azure Datacenter - westus","ResourceGroupName":"Test123","'+catalogName+'_simplevm_rgName":"Infra-needs-do-not-delete","'+catalogName+'_simplevm_subnet":"Seattle_dev_subnet"}';
						def orderFormData = '{"AzureTf_image_sku":"16.04-LTS","CloudAccount":"Azure DK","AzureTf_address_space":"10.0.0.0/16","AzureTf_hostname":"tf'+buildNumber+'","LeaseEndDate":"","AzureTf_admin_username":"vmadmin","UserGroup":"cfcbad03d711110050f5edcb9e61038f","ScheduleTimeZone":"America/Los_Angeles","SubscriptionId":"$(context.ServiceAccountID)","CostCenter":"","AzureTf_image_offer":"UbuntuServer","AzureTf_test2":"vmadmin","AzureTf_image_publisher":"Canonical","ResourceGroup":" ","CreateResourceGroup":true,"AzureTf_admin_password":"admin01!","AzureTf_prefix":"test78'+buildNumber+'","AzureTf_resourceGroup":"Demoden20'+buildNumber+'","AzureTf_subnet_prefix":"10.0.10.0/24","ScheduleProfile":"-- No Schedule --","BusinessService":"","AzureTf_vmSize":"Standard_D1_v2","AzureTf_image_version":"latest","Application":"","StackName":"test28'+buildNumber+'","Location":"Azure Datacenter - australiacentral","ResourceGroupName":""}';
						//'{"CloudAccount":"AzureCA","ScheduleProfile":"-- No Schedule --","UserGroup":"cfcbad03d711110050f5edcb9e61038f","ScheduleTimeZone":"America/Los_Angeles","BusinessService":"","'+catalogName+'_demoinfra_prefix":"'+catalogName+'","CostCenter":"","CreateResourceGroup":"Yes","Application":"","StackName":"'+stackName+'","Location":"Azure Datacenter - westus","ResourceGroupName":"'+catalogName+'","JenkinsPipeline2_simplevm_rgName":"anyRGName","JenkinsPipeline2_simplevm_hradwareType":"hardware","JenkinsPipeline2_simplevm_network":"network","JenkinsPipeline2_simplevm_region":"region","JenkinsPipeline2_simplevm_subnet":"subnet","JenkinsPipeline2_simplevm_vmname":"vmname"}';
						//def request_body_map = "{\"CloudAccount\": \"AzureAcct\", \"ScheduleProfile\": \"-- No Schedule --\", \"UserGroup\": \"cfcbad03d711110050f5edcb9e61038f\", \"ScheduleTimeZone\": \"America/Los_Angeles\", \"BusinessService\": \"\", \"JenkinsPipeline6_demoinfra_prefix\": \"Jenkins6\", \"CostCenter\": \"\", \"CreateResourceGroup\": \"Yes\", \"Application\": \"\", \"StackName\": \"JenkinsStackDemo2\", \"Location\": \"Azure Datacenter - westus\", \"ResourceGroupName\": \"JenkinsDemo2\"}";

						println 'Order form data:'+orderFormData
						post.setRequestMethod("POST")
						post.setDoOutput(true)
						post.setRequestProperty("Content-Type", "application/json; utf-8");
						post.setRequestProperty("Accept", "application/json");
						post.setRequestProperty("Authorization", auth)
						post.getOutputStream().write(orderFormData.getBytes("UTF-8"));
						def postRC = post.getResponseCode();
						println 'order catalog status code:'+postRC
						if (postRC.equals(200)) {
							def response = post.getInputStream().getText();
							println 'order catalog status response:'+response
							//parser = new JsonSlurper()
							println("Response received on order placement:"+response);
							def jsonResp = parseJsonText(response);
							def count = 0;
							def status = 'Processing'
							while (count < 200) {
								reqItem = jsonResp.number;
								def url_snow_status = baseURL + '/api/now/cmp_catalog_api/status?req_item=' + jsonResp.number;
								get = new URL(url_snow_status).getText(
										connectTimeout: 5000,
										readTimeout: 10000,
										useCaches: true,
										requestProperties: ['Authorization': auth]);
								try{
									println("Response received on status:"+get);
									getResp = parseJsonText(get);
									status = getResp.state;
									println('Current Deployment State  ' + status);
									if (status.equals('Closed Complete') || status.contains('Complete')) {
										println('Deployment Completed');
										break;
									}
									if (status.equals('Error')) {
										println('Deployment Failed');
										throw new Exception('Error');
										break;
									}
										count++;
									Thread.sleep(1000 * 30)
								} catch(e) {
									println("Exception reported:"+e);
								}
							}
							println("Stacks Created ")
						} else {
							println 'Order failed status code:'+postRC;
							throw new Exception('Error:'+postRC);
						}
					} catch (e) {
						println 'Order failed status code:'+e.getStackTrace().toString();
					}
				}
            }
        }
		stage('Post provision Software Installation') {
            steps {
                snDevOpsStep(enabled:true,ignoreErrors:true)
                println 'Post provision Software Installation'
            }
        }
		stage('Artifact Deployment') {
            steps {
		        snDevOpsStep(enabled:true,ignoreErrors:true)
                println 'Deploy Artifact on the infrastructure'
            }
        }
    }
}
