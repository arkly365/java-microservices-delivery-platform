pipeline {
    agent any

    stages {
        stage('Init') {
            steps {
                echo 'Phase 21 microservices pipeline start'
                sh 'pwd'
                sh 'ls -la'
            }
        }

        stage('Build service-a') {
            steps {
                dir('service-a') {
                    sh 'mvn clean package -DskipTests'
                    sh 'docker build -t service-a:local .'
                }
            }
        }

        stage('Build service-b') {
            steps {
                dir('service-b') {
                    sh 'mvn clean package -DskipTests'
                    sh 'docker build -t service-b:local .'
                }
            }
        }

        stage('Deploy microservices') {
            steps {
                sh '''
                    docker rm -f service-a-dev service-b-dev service-a-prod service-b-prod || true
                    docker compose -f docker-compose.deploy.yml up -d
                '''
            }
        }

		stage('Verify hello endpoints') {
			steps {
				sh 'curl -f http://service-a-dev:8080/hello'
				sh 'curl -f http://service-b-dev:8080/hello'
				sh 'curl -f http://service-a-prod:8080/hello'
				sh 'curl -f http://service-b-prod:8080/hello'
			}
		}

		stage('Verify health endpoints') {
			steps {
				sh 'curl -f http://service-a-dev:8080/actuator/health'
				sh 'curl -f http://service-b-dev:8080/actuator/health'
				sh 'curl -f http://service-a-prod:8080/actuator/health'
				sh 'curl -f http://service-b-prod:8080/actuator/health'
			}
		}
    }
}