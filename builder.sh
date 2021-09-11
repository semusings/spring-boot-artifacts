#!/usr/bin/env sh

SCRIPT_DIR="$(dirname "$0")"

assert_value() {
  if [ -z "$1" ]; then
    echo "No args: $2"
    exit 1
  fi
}

MVN_CMD="./mvnw -B -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn -V"

# FUNCTIONS
# shellcheck disable=SC2059
print(){ printf "$@"; }
# shellcheck disable=SC2145
# shellcheck disable=SC2059
println(){ printf "$@\n"; }

build_parent(){
  $MVN_CMD clean install -f spring-boot-artifacts-parent/pom.xml
}

deploy_parent(){
  $MVN_CMD clean deploy -P release -DskipTests=true -f spring-boot-artifacts-parent/pom.xml
}

build(){
  build_parent
  ## Execute Command on Artifacts
  while IFS=, read -r artifactId tag; do
    echo "--------------------------------------------------------------------------"
    echo "=> $artifactId:$tag"
    echo "--------------------------------------------------------------------------"
    $MVN_CMD clean versions:set -DnewVersion="$tag" -f "$artifactId/pom.xml"
    $MVN_CMD package -f "$artifactId/pom.xml"
  done <"$SCRIPT_DIR/artifacts.csv"
}

deploy_artifacts(){
  ## Execute Command on Artifacts
  while IFS=, read -r artifactId version; do
    echo "--------------------------------------------------------------------------"
    echo "=> $artifactId:$version"
    echo "--------------------------------------------------------------------------"
    $MVN_CMD clean versions:set -DnewVersion="$tag" -f "$artifactId/pom.xml"
    $MVN_CMD deploy -P release -DskipTests=true -f "$artifactId/pom.xml"
  done <"$SCRIPT_DIR/artifacts.csv"
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
  "deploy_parent") deploy_parent ;;
  "deploy_artifacts") deploy_artifacts ;;
  "config_maven") config_maven ;;
  "config_gpg") config_gpg ;;
  *)
    cat <<EOF | sed 's/^[ \t]*//'
      Usage: $0 <OPTION>

      Where OPTION is one of the following:
      - build
      - deploy_parent - deploys generated parent artifacts into Maven Central
      - deploy_artifacts - deploys generated artifacts into Maven Central
      - config_maven - configure Maven to publish into Sonatype Staging Repository
      - config_gpg - configure GPG key from the env variable

EOF
    exit 1
  ;;
esac
