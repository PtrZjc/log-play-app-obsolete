import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import pl.zajacp.App
import java.io.File
import java.io.InputStreamReader


private const val APPLICATION_NAME = "wyniki-gier"
private const val TOKENS_DIRECTORY_PATH = "tokens"
private const val CREDENTIALS_FILE_PATH = "/credentials.json"
private const val spreadsheetId = "1oQShqTRg6vMdti5tES9aooSv_DlwEub1C2O7xMWA7eQ"

private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

fun main(){
    //to check if works only
    getValuesFromRange("All_Wyniki!A1:B10").let { println(it) }
}

fun getValuesFromRange(range: String): List<List<String>> {
    val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
    val service = Sheets.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
        .setApplicationName(APPLICATION_NAME)
        .build()
    val response = service.spreadsheets().values()[spreadsheetId, range]
        .execute()
    return response.getValues().map { row -> row.map { colValue -> colValue.toString() } }
}

private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential =
    App::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
        .let { GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(it)) }
        .let {
            GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, it, listOf(SheetsScopes.SPREADSHEETS_READONLY)
            )
                .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build()
        }.let {
            AuthorizationCodeInstalledApp(
                it,
                LocalServerReceiver.Builder().setPort(8888).build()
            ).authorize("user")
        }