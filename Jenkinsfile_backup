pipeline {
    agent any

    stages {
        stage('Init') {
            steps {
                echo 'Phase 22 microservices pipeline with private registry start'
                sh 'pwd'
                sh 'ls -la'
                echo "BRANCH_NAME=${env.BRANCH_NAME}"
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

        stage('Push service-a to private registry') {
            steps {
                sh '''
                    docker tag service-a:local 127.0.0.1:5000/service-a:latest
                    docker push 127.0.0.1:5000/service-a:latest
                '''
            }
        }

        stage('Push service-b to private registry') {
            steps {
                sh '''
                    docker tag service-b:local 127.0.0.1:5000/service-b:latest
                    docker push 127.0.0.1:5000/service-b:latest
                '''
            }
        }

        stage('Deploy dev') {
            when {
                branch 'develop'
            }
            steps {
                sh '''
                    docker rm -f service-a-dev service-b-dev || true
                    docker compose -f docker-compose.dev.yml up -d
                '''
            }
        }

        stage('Deploy prod') {
            when {
                branch 'main'
            }
            steps {
                sh '''
                    docker rm -f service-a-prod service-b-prod || true
                    docker compose -f docker-compose.prod.yml up -d
                '''
            }
        }

        stage('Wait for services') {
            steps {
                sh 'sleep 15'
            }
        }

        stage('Verify dev hello endpoints') {
            when {
                branch 'develop'
            }
            steps {
                sh '''
                    for url in \
                      http://service-a-dev:8080/hello \
                      http://service-b-dev:8080/hello
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

        stage('Verify dev health endpoints') {
            when {
                branch 'develop'
            }
            steps {
                sh '''
                    for url in \
                      http://service-a-dev:8080/actuator/health \
                      http://service-b-dev:8080/actuator/health
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

        stage('Verify prod hello endpoints') {
            when {
                branch 'main'
            }
            steps {
                sh '''
                    for url in \
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

        stage('Verify prod health endpoints') {
            when {
                branch 'main'
            }
            steps {
                sh '''
                    for url in \
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