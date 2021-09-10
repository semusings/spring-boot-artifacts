# GPG Commands

```
gpg --full-gen-key

gpg --list-keys

<get last 8 letter from keyID>

gpg --keyserver keyserver.ubuntu.com --send-keys <keyId>

gpg --export-secret-keys <keyId> | base64
```

## Repositories

- Maven

```xml

<repositories>
    <repository>
        <id>maven-releases</id>
        <url>https://oss.sonatype.org/service/local/staging/deploy/maven2</url>
        <layout>default</layout>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
    <repository>
        <id>maven-snapshots</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <layout>default</layout>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

- Gradle

```
repositories {
    mavenCentral()
    maven {
        url "https://oss.sonatype.org/service/local/staging/deploy/maven2"
        mavenContent {
            releasesOnly()
        }
    }
    maven {
        url "https://oss.sonatype.org/content/repositories/snapshots"
        mavenContent {
            snapshotsOnly()
        }
    }
}
```
