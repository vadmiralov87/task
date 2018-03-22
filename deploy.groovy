#!/usr/bin/env groovy

node {
	stage('Deploy') {
		sh '''
			sudo docker-machine create --driver amazonec2 --amazonec2-region eu-central-1 --amazonec2-security-group launch-wizard-55 --amazonec2-instance-type t2.micro vadmiralov.2
           	#не забыть  поменять!!!!sudo docker-machine create --driver amazonec2 --amazonec2-region eu-west-1 --amazonec2-security-group launch-wizard-55 --amazonec2-instance-type t2.micro vadmiralov.2
		'''
	}
}