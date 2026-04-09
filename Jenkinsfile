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
				sh '''
					for url in \
					  http://service-a-dev:8080/hello \
					  http://service-b-dev:8080/hello \
					  http://service-a-prod:8080/hello \
					  http://service-b-prod:8080/hello
					do
					  echo "Checking $url"
					  for i in 1 2 3 4 5 6 7 8 9 10
					  do
						curl -f "$url" && break
						echo "Retry $i for $url..."
						sleep 5
					  done
					  curl -f "$url"
					done
				'''
			}
		}

		stage('Verify health endpoints') {
			steps {
				sh '''
					for url in \
					  http://service-a-dev:8080/actuator/health \
					  http://service-b-dev:8080/actuator/health \
					  http://service-a-prod:8080/actuator/health \
					  http://service-b-prod:8080/actuator/health
					do
					  echo "Checking $url"
					  for i in 1 2 3 4 5 6 7 8 9 10
					  do
						curl -f "$url" && break
						echo "Retry $i for $url..."
						sleep 5
					  done
					  curl -f "$url"
					done
				'''
			}
		}
		
		
		
    }
}