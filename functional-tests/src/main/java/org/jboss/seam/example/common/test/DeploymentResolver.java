package org.jboss.seam.example.common.test;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact;
import org.jboss.shrinkwrap.resolver.api.maven.PackagingType;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;

public class DeploymentResolver {

    public static Archive<?> createDeployment() {

        // use profiles defined in 'maven.profiles' property in pom.xml
        String profilesString = System.getProperty("maven.profiles");
        String[] profiles = profilesString != null ? profilesString.split(", ?") : new String[0];
        PomEquippedResolveStage mvn = Maven.resolver().loadPomFromFile("pom.xml", profiles);

        // resolves an artifact with coordinates given in property DEPLOYMENT_ARTIFACT in /ftest.properties
        MavenResolvedArtifact artifact = mvn.resolve(SeamGrapheneTest.getProperty("DEPLOYMENT_ARTIFACT"))
                .withoutTransitivity().asSingleResolvedArtifact();

        // use correct archive type
        PackagingType packaging = artifact.getCoordinate().getPackaging();
        Class<? extends Archive<?>> deploymentClass = EnterpriseArchive.class;
        if (packaging == PackagingType.WAR) {
            deploymentClass = WebArchive.class;
        }

        //  this is ugly due to ARQ-1390; need to get rid of dots in archive name, otherwise it would be
        //  return ShrinkWrap.createFromZipFile(deploymentClass, artifact.asFile()).as(deploymentClass);
        return ShrinkWrap.create(ZipImporter.class, artifact.getCoordinate().getArtifactId() + "." + packaging)
                .importFrom(artifact.asFile()).as(deploymentClass);
    }
}
