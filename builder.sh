#!/usr/bin/env sh
set -e
# shellcheck disable=SC2046
cd $(dirname "$0")

MVN_CMD="./mvnw -B -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn -V"

# FUNCTIONS
# shellcheck disable=SC2059
print(){ printf "$@"; }
# shellcheck disable=SC2145
# shellcheck disable=SC2059
println(){ printf "$@\n"; }

build_parent(){
  cd spring-boot-artifacts-parent && $MVN_CMD clean install
}

build(){
  build_parent
  $MVN_CMD clean package
}

deploy_local(){
  $MVN_CMD clean install -DskipTests=true
}

deploy_remote(){
  $MVN_CMD clean deploy -P release -DskipTests=true
}

config_gpg(){
  if [ "$GPG_SIGNING_KEY" = "" ]; then
    println "ERROR: No GPG_SIGNING_KEY defined"
    exit 200
  fi

  # shellcheck disable=SC2155
  export GPG_TTY=$(tty)
  mkdir -p ~/.gnupg/
  print "${GPG_SIGNING_KEY}" | base64 --decode > ~/.gnupg/private.key
  gpg --import ~/.gnupg/private.key
}

config_maven(){
  if [ "$OSSRH_USERNAME" = "" -o "$OSSRH_PASSWORD" = "" ]; then
    println "ERROR: Variables OSSRH_USERNAME or OSSRH_PASSWORD not defined"
    exit 201

  fi

  cat <<EOF> ~/.m2/settings.xml
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>${OSSRH_USERNAME}</username>
      <password>${OSSRH_PASSWORD}</password>
    </server>
  </servers>
</settings>
EOF
}

# MAIN
case $1 in
  "bump") bump_version ;;
  "build") build ;;
  "deploy_local"|"local") deploy_local ;;
  "deploy"|"remote") deploy_remote ;;
  "config_maven") config_maven ;;
  "config_gpg") config_gpg ;;
  *)
    cat <<EOF | sed 's/^[ \t]*//'
      Usage: $0 <OPTION>

      Where OPTION is one of the following:
      - build
      - local - deploys generated artifacts locally
      - remote - deploys generated artifacts into Maven Central
      - deploy (same as 'remote')
      - deploy_local (same as 'local')
      - config_maven - configure Maven to publish into Sonatype Staging Repository
      - config_gpg - configure GPG key from the env variable

EOF
    exit 1
  ;;
esac
