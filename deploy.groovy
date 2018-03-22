#!/usr/bin/env groovy

node {
	stage('Deploy') {
		sh '''
           	docker-machine create --driver amazonec2 --amazonec2-region eu-west-1 --amazonec2-security-group launch-wizard-55 --amazonec2-instance-type t2.micro vadmiralov.2
           	eval $(docker-machine env vadmiralov.2)
           	docker run -d -p 80:80 vadmiralov87/nginx
           	echo "http://"`docker-machine ip vadmiralov.2`
           	eval $(docker-machine env -u)
		'''
	}
}