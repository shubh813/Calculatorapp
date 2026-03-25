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
                    # Remove old WAR and extracted folder first
                    docker exec ${TOMCAT_CONTAINER} rm -rf ${DEPLOY_PATH}calculator-app.war
                    docker exec ${TOMCAT_CONTAINER} rm -rf ${DEPLOY_PATH}calculator-app

                    # Copy new WAR into container
                    docker cp ${WAR_FILE} ${TOMCAT_CONTAINER}:${DEPLOY_PATH}

                    # Restart Tomcat to pick up new WAR
                    docker exec ${TOMCAT_CONTAINER} /usr/local/tomcat/bin/shutdown.sh || true
                    sleep 5
                    docker exec ${TOMCAT_CONTAINER} /usr/local/tomcat/bin/startup.sh

                    echo "✅ WAR copied and Tomcat restarted!"
                '''
                sh 'sleep 15'
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
                    echo "✅ Successfully deployed commit: $DEPLOYED_COMMIT"
                '''
            }
        }
        stage('Health Check') {
            steps {
                sh '''
                    HEALTH=$(curl -s ${APP_URL}/api/calculator/health)
                    echo "💚 Health Status: $HEALTH"
                '''
            }
        }
    }
    post {
        success {
            echo '''
            ╔══════════════════════════════════╗
            ║   ✅ DEPLOYMENT SUCCESSFUL!      ║
            ║   Calculator App is LIVE!        ║
            ╚══════════════════════════════════╝
            '''
        }
        failure {
            echo '''
            ╔══════════════════════════════════╗
            ║   ❌ DEPLOYMENT FAILED!          ║
            ║   Check logs above for errors    ║
            ╚══════════════════════════════════╝
            '''
        }
    }
}
