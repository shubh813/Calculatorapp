pipeline {
    agent any
    tools {
        maven 'Maven3'
    }
    environment {
        TOMCAT_CONTAINER = 'tomcat-demo'
        WAR_FILE = 'target/calculator-app.war'
        DEPLOY_PATH = '/usr/local/tomcat/webapps/'
        APP_URL = 'http://192.168.1.54:8090/calculator-app'
    }
    stages {
        stage('Checkout') {
            steps {
                echo 'Pulling latest code from GitHub...'
                checkout scm
            }
        }
        stage('Show Commit Info') {
            steps {
                echo 'Building from commit:'
                sh 'git log --oneline -1'
                sh 'git branch'
            }
        }
        stage('Build WAR') {
            steps {
                echo 'Building WAR file with Maven...'
                sh 'mvn clean package -DskipTests'
                echo 'WAR file built successfully!'
            }
        }
        stage('Deploy to Tomcat') {
            steps {
                echo 'Deploying WAR to Tomcat...'
                sh 'docker stop tomcat-demo || true'
                sh 'docker rm tomcat-demo || true'
                sh 'docker run -d --name tomcat-demo -p 8090:8080 tomcat:latest'
                sh 'sleep 10'
                sh 'docker cp target/calculator-app.war tomcat-demo:/usr/local/tomcat/webapps/'
                sh 'sleep 20'
                echo 'Deployment complete!'
            }
        }
        stage('Verify Deployment') {
            steps {
                echo 'Verifying deployment...'
                sh 'curl -s http://192.168.1.54:8090/calculator-app/info'
            }
        }
        stage('Health Check') {
            steps {
                sh 'curl -s http://192.168.1.54:8090/calculator-app/api/calculator/health'
            }
        }
    }
    post {
        success {
            echo 'DEPLOYMENT SUCCESSFUL!'
        }
        failure {
            echo 'DEPLOYMENT FAILED!'
        }
    }
}
