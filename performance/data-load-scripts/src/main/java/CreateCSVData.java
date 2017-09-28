import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.sql.Timestamp;
import java.util.UUID;

public class CreateCSVData{
    public static final String GROUP_NAME_FORMAT = "group-%d";
    public static final String ZONE_ID_FORMAT = "zone-%d";

    public static final String GROUP_GUID_FORMAT = "group-guid-%d-%d";
    public static final String USER_GUID_FORMAT = "user-guid-%d-%d";

    public static void main(String[] args) {
        System.out.println("Generating CSV files");

        int zones = Integer.parseInt(System.getProperty("numOfZones"));

        printZones(zones);
        printDefaultGroups(zones);
        printGeneratedGroups(zones);
        printUsers(zones);
        printClients(zones);
        printIDPs(zones);
        System.out.println("Files created!!");
    }

    public static void printZones(int numberOfZones) {
        StringBuffer csvData = new StringBuffer();
        csvData.append("id,created,lastmodified,version,subdomain,name,description,config");
        String config = "\"{\\\"clientLockoutPolicy\\\":{\\\"lockoutPeriodSeconds\\\":-1,\\\"lockoutAfterFailures\\\":-1,\\\"countFailuresWithin\\\":-1},\\\"tokenPolicy\\\":{\\\"accessTokenValidity\\\":-1,\\\"refreshTokenValidity\\\":-1,\\\"jwtRevocable\\\":false,\\\"refreshTokenUnique\\\":false,\\\"refreshTokenFormat\\\":\\\"jwt\\\",\\\"activeKeyId\\\":null},\\\"samlConfig\\\":{\\\"assertionSigned\\\":true,\\\"requestSigned\\\":true,\\\"wantAssertionSigned\\\":true,\\\"wantAuthnRequestSigned\\\":false,\\\"assertionTimeToLiveSeconds\\\":600},\\\"corsPolicy\\\":{\\\"xhrConfiguration\\\":{\\\"allowedOrigins\\\":[\\\".*\\\"],\\\"allowedOriginPatterns\\\":[],\\\"allowedUris\\\":[\\\".*\\\"],\\\"allowedUriPatterns\\\":[],\\\"allowedHeaders\\\":[\\\"Accept\\\",\\\"Authorization\\\",\\\"Content-Type\\\"],\\\"allowedMethods\\\":[\\\"GET\\\"],\\\"allowedCredentials\\\":false,\\\"maxAge\\\":1728000},\\\"defaultConfiguration\\\":{\\\"allowedOrigins\\\":[\\\".*\\\"],\\\"allowedOriginPatterns\\\":[],\\\"allowedUris\\\":[\\\".*\\\"],\\\"allowedUriPatterns\\\":[],\\\"allowedHeaders\\\":[\\\"Accept\\\",\\\"Authorization\\\",\\\"Content-Type\\\"],\\\"allowedMethods\\\":[\\\"GET\\\"],\\\"allowedCredentials\\\":false,\\\"maxAge\\\":1728000}},\\\"links\\\":{\\\"logout\\\":{\\\"redirectUrl\\\":\\\"/login\\\",\\\"redirectParameterName\\\":\\\"redirect\\\",\\\"disableRedirectParameter\\\":false,\\\"whitelist\\\":null},\\\"selfService\\\":{\\\"selfServiceLinksEnabled\\\":true,\\\"signup\\\":\\\"/create_account\\\",\\\"passwd\\\":\\\"/forgot_password\\\"}},\\\"prompts\\\":[{\\\"name\\\":\\\"username\\\",\\\"type\\\":\\\"text\\\",\\\"text\\\":\\\"Email\\\"},{\\\"name\\\":\\\"password\\\",\\\"type\\\":\\\"password\\\",\\\"text\\\":\\\"Password\\\"},{\\\"name\\\":\\\"passcode\\\",\\\"type\\\":\\\"password\\\",\\\"text\\\":\\\"One Time Code (Get on at /passcode)\\\"}],\\\"idpDiscoveryEnabled\\\":false,\\\"accountChooserEnabled\\\":false}\"";
        int i=0;
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        while(i++ < numberOfZones) {
            String zoneId = String.format(ZONE_ID_FORMAT, i);
            csvData.append("\n" + zoneId + "," + ts.toString() + "," + ts.toString() + ",0 ," + zoneId + "," + zoneId + ",Performance test zone," +config);
        }
        Path file = Paths.get("identity_zone.csv");
        try {
            Files.write(file, Arrays.asList(csvData.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printGeneratedGroups(int numberOfZones) {
        int numberOfGroups = Integer.parseInt(System.getProperty("groupsPerZone"));
        StringBuffer csvData = new StringBuffer();
        csvData.append("id,displayName,created,lastmodified,version,identity_zone_id,description");

        int i=0;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        while(i++ < numberOfZones) {
            for(int j = 0; j < numberOfGroups; j++){
                String groupName = String.format(GROUP_NAME_FORMAT, j);
                String guid = String.format(GROUP_GUID_FORMAT, i, j);
                String zoneId = String.format(ZONE_ID_FORMAT, i);
                csvData.append("\n" +guid+ ","+groupName+ "," + timestamp.toString() + "," + timestamp.toString() + ",0 ," + zoneId + ",NULL");
            }
        }
        Path file = Paths.get("generated_groups.csv");
        try {
            Files.write(file, Arrays.asList(csvData.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printDefaultGroups(int numberOfZones) {
        StringBuffer csvData = new StringBuffer();
        csvData.append("id,displayName,created,lastmodified,version,identity_zone_id,description");
        List<String> scopes = Arrays.asList("clients.admin","clients.read","clients.secret","clients.write","groups.update","idps.read","idps.write","oauth.login","password.write","scim.create","scim.read","scim.userids","scim.write","scim.zones","uaa.admin");

        int i=0;
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        while(i++ < numberOfZones) {
            for(String scope: scopes) {
                String zoneId = String.format(ZONE_ID_FORMAT, i);

                String guid = UUID.randomUUID().toString();
                csvData.append("\n" +guid+ ","+scope+ "," + ts.toString() + "," + ts.toString() + ",0 ," + zoneId + ",NULL");
            }
        }
        Path file = Paths.get("default_groups.csv");
        try {
            Files.write(file, Arrays.asList(csvData.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printUsers(int numberOfZones) {
        int numberOfUsers = Integer.parseInt(System.getProperty("usersPerZone"));
        StringBuffer csvData = new StringBuffer();
        csvData.append("\"id\",\"created\",\"lastmodified\",\"version\",\"username\",\"password\",\"email\",\"authorities\",\"givenname\",\"familyname\",\"active\",\"phonenumber\",\"verified\",\"origin\",\"external_id\",\"identity_zone_id\",\"salt\",\"passwd_lastmodified\",\"legacy_verification_behavior\",\"passwd_change_required\",\"last_logon_success_time\",\"previous_logon_success_time\"");
        int i=0,j=0;
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String date = ts.toString();
        while(i++ < numberOfZones){
            while(j++ < numberOfUsers) {
                String guid = String.format(USER_GUID_FORMAT, i, j);
                String zoneId = String.format(ZONE_ID_FORMAT, i);

                csvData.append("\n"+guid+","+ date + "," +date+ ",0,user"+j+",$2a$10$v92nQ.g5dXQ1V1svF.KO4.I4YIWtzNlmnBGrJjB94wLheboASLaoG,user"+j+"@testcf.com,uaa.user,Perf"+j+"FN,Perf"+j+"LN,1,NULL,1,uaa,NULL," + zoneId + ",NULL,"+date+",0,0,NULL,NULL");
            }
            j=0;
        }
        Path file = Paths.get("users.csv");
        try {
            Files.write(file, Arrays.asList(csvData.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printClients(int numberOfZones) {
        StringBuffer csvData = new StringBuffer();
        csvData.append("\"client_id\",\"resource_ids\",\"client_secret\",\"scope\",\"authorized_grant_types\",\"web_server_redirect_uri\",\"authorities\",\"access_token_validity\",\"refresh_token_validity\",\"additional_information\",\"autoapprove\",\"identity_zone_id\",\"lastmodified\",\"show_on_home_page\",\"app_launch_url\",\"app_icon\",\"created_by\",\"required_user_groups\"");
        int i=0;

        while(i++ < numberOfZones){
            addClientsForType(csvData, i, Integer.parseInt(System.getProperty("clientsPerZone.clientCredentials")), "client_credentials");
            addClientsForType(csvData, i, Integer.parseInt(System.getProperty("clientsPerZone.password")), "password");
            addClientsForType(csvData, i, Integer.parseInt(System.getProperty("clientsPerZone.authcode")), "authorization_code");
            addClientsForType(csvData, i, Integer.parseInt(System.getProperty("clientsPerZone.implicit")), "implicit");
        }

        Path file = Paths.get("oauth_client_details.csv");
        try {
            Files.write(file, Arrays.asList(csvData.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void addClientsForType(StringBuffer csvData, int zoneCounter, int numOfClients, String grantType) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String date = ts.toString();

        for (int j = 0; j < numOfClients; j++) {
            // TODO: Refactor to split by grant type
            String zoneId = String.format(ZONE_ID_FORMAT, zoneCounter);
            String clientId = String.format("client-%d-%s-%d", zoneCounter, grantType, j);
            csvData.append("\n\"" + clientId + "\",\"none\",\"$2a$10$YhCmy5KLFs60yUn4.NgnFO4FsxxclNtwK8cEg8dBFUTvZgG20m4gG\",\"openid,password.me\",\"" + grantType + "\",\"http://localhost\",\"clients.read,clients.secret,idps.write,uaa.resource,zones.perfzone1.admin,clients.write,clients.admin,scim.write,idps.read,scim.read\",NULL,NULL,\"{\\\"allowedproviders\\\":[\\\"uaa\\\"],\\\"scopes\\\":[\\\"uaa.resource\\\",\\\"scim.read\\\"]}\",\"openid\",\"" + zoneId + "\",\""+date+"\",1,NULL,NULL,NULL,NULL");
        }
    }

    public static void printIDPs(int numberOfZones) {
        StringBuffer csvData = new StringBuffer();
        csvData.append("\"id\",\"created\",\"lastmodified\",\"version\",\"identity_zone_id\",\"name\",\"origin_key\",\"type\",\"config\",\"active\"");
        int i=0;
        int j=0;

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String date = ts.toString();
        String guid, created, lastModified, version, identityZoneId, name, originKey, type, config, active;

        while(i++ < numberOfZones){
            String zoneId = String.format(ZONE_ID_FORMAT, i);

            guid = UUID.randomUUID().toString();
            created = date;
            lastModified = date;
            version = "0";
            identityZoneId = zoneId;
            name =  "perfidp" + i;
            originKey = "uaa";
            type = "uaa";
            config = "\"{\\\"emailDomain\\\":null,\\\"additionalConfiguration\\\":null,\\\"providerDescription\\\":null,\\\"passwordPolicy\\\":null,\\\"lockoutPolicy\\\":null,\\\"disableInternalUserManagement\\\":false}\"";
            active = "1";
            csvData.append(String.format("\n%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", guid, created, lastModified, version, identityZoneId, name, originKey, type, config, active));
        }
        Path file = Paths.get("identity_provider.csv");
        try {
            Files.write(file, Arrays.asList(csvData.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
