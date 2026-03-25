pipeline {
    agent any
    tools {
        maven 'Maven3'
    }
    environment {
        TOMCAT_CONTAINER = 'tomcat-demo'
        WAR_FILE = 'target/calculator-app.war'
        DEPLOY_PATH = '/usr/local/tomcat/webapps/'
        APP_URL = 'http://localhost:8090/calculator-app'
    }
    stages {
        stage('Checkout') {
            steps {
                echo '📥 Pulling latest code from GitHub...'
                checkout scm
            }
        }
        stage('Show Commit Info') {
            steps {
                echo '🔍 Building from commit:'
                sh 'git log --oneline -1'
                sh 'git branch'
            }
        }
        stage('Build WAR') {
            steps {
                echo '🔨 Building WAR file with Maven...'
                sh 'mvn clean package -DskipTests'
                echo '✅ WAR file built successfully!'
            }
        }
        stage('Deploy to Tomcat') {
            steps {
                echo '🚀 Deploying WAR to Tomcat Docker container...'
                sh '''
                    # Stop and remove old Tomcat container completely
                    docker stop ${TOMCAT_CONTAINER} || true
                    docker rm ${TOMCAT_CONTAINER} || true

                    # Start fresh Tomcat container
                    docker run -d \
                        --name ${TOMCAT_CONTAINER} \
                        -p 8090:8080 \
                        tomcat:latest

                    # Wait for Tomcat to start
                    sleep 10

                    # Copy WAR into fresh container
                    docker cp ${WAR_FILE} ${TOMCAT_CONTAINER}:${DEPLOY_PATH}
                    echo "✅ WAR deployed to fresh Tomcat!"
                '''
                sh 'sleep 20'
                echo '✅ Deployment complete!'
            }
        }
        stage('Verify Deployment') {
            steps {
                echo '🔍 Verifying deployment...'
                sh '''
                    RESPONSE=$(curl -s ${APP_URL}/info)
                    echo "📋 App Info Response:"
                    echo $RESPONSE
                    DEPLOYED_COMMIT=$(echo $RESPONSE | grep -o '"gitCommitShort":"[^"]*"' | cut -d'"' -f4)
                    echo ""
                    echo "✅ Successfully deploye
