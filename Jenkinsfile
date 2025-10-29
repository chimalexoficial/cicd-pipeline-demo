pipeline {
  agent any

  environment {
    SONARQUBE = 'SonarQube'                                 // Jenkins → System → SonarQube servers (Name)
    NEXUS_URL = 'http://116.203.222.206:8081/repository/maven-releases'
    NEXUS_CREDS = credentials('nexus-deployer')             // Jenkins credential ID
    // SLACK_WEBHOOK = credentials('slack-webhook')         // Uncomment if you added Slack
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build & Test') {
      steps { sh 'mvn -B clean package' }
      post { always { junit 'target/surefire-reports/*.xml' } }
    }

    stage('SonarQube Analysis') {
      steps {
        withSonarQubeEnv('SonarQube') {
          sh 'mvn sonar:sonar'
        }
      }
    }

    stage('Upload to Nexus') {
      steps {
        sh '''
          FILE=$(ls target/*-1.0.0.jar | head -n1)
          curl -s -u ${NEXUS_CREDS_USR}:${NEXUS_CREDS_PSW} \
               --upload-file "$FILE" \
               "${NEXUS_URL}/com/example/hello-ci/1.0.0/hello-ci-1.0.0.jar"
        '''
      }
    }
  }

  post {
    success {
      echo "✅ Build + Test + Sonar + Nexus: OK"
      // sh '''
      //   curl -X POST -H "Content-type: application/json" \
      //     --data "{\\"text\\":\\"✅ ${JOB_NAME} #${BUILD_NUMBER} succeeded! <${BUILD_URL}|Open> \\"}" \
      //     "$SLACK_WEBHOOK"
      // '''
    }
    failure {
      echo "❌ Pipeline failed"
      // sh '''
      //   curl -X POST -H "Content-type: application/json" \
      //     --data "{\\"text\\":\\"❌ ${JOB_NAME} #${BUILD_NUMBER} failed! <${BUILD_URL}|Open> \\"}" \
      //     "$SLACK_WEBHOOK"
      // '''
    }
  }
}

