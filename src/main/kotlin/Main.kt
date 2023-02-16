import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import model.ModelNews
import model.New
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


val client = OkHttpClient.Builder().build()
val test02 = GsonBuilder().setPrettyPrinting()
// Create request
val request = Request
    .Builder()
    .url("https://api2.kiparo.com/static/it_news.json")
    .build()

suspend fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
//    var scanner = Scanner(System.in)
//
//    var num = scanner.nextInt()

    var yourName = readLine()

    if(yourName == "Duman") {
        println("Hello, You are welcome!")
        getResult()
    } else {
        println("You are a faggot!")
    }

}

suspend fun getResult() {

    coroutineScope {
        launch {
            val result = client.newCall(request).execute()
            println("${result.code}: ${result.body}")
            val gson = Gson()
            val resFromWeb = result.body?.string()

            println("${resFromWeb}")

            val gson02 = GsonBuilder().setPrettyPrinting().create()
            val company = gson02.fromJson(resFromWeb, ModelNews::class.java)

            val prettyJsonString01 = gson02.toJson(company)

            println("-------------")
            //конвертируем в нужную коллекцию
            val pickedData = gson.fromJson(prettyJsonString01,ModelNews::class.java)
            println(pickedData)
            //ч/р форич присваиваем значение стринговой даты к полу с  типом ЛокалДэйт
            pickedData.news.forEach {
//                println("==========")
//                println(it.title)
//                println(it.description)
                it.dateLikeDateType = givenString_whenCustomFormat_thenLocalDateTimeCreated(it.date)
                //println(it.dateLikeDateType)
//                println("==========")
//                println("")
            }

            //создаем новый изменяемы список для отсортировки новостей по дате
            val sortedNewsByDateMutableList: MutableList<New> = mutableListOf()
            sortedNewsByDateMutableList.addAll(pickedData.news)
            sortedNewsByDateMutableList.sortBy { it.dateLikeDateType }

            //создаем массив, для реализации поиска через фильтр
            val filteredMutableList = sortedNewsByDateMutableList.filter { it.keywords.any{valueText-> valueText.contains("google")} }

            //отображение отсортированных новостей по дате
            sortedNewsByDateMutableList.forEach {
                println("+++++++")
                println(it.dateLikeDateType)
                println(it.date)
                println(it.title)
                println(it.description)
                println(it.keywords)
            }

            //фильтр по поиску
            filteredMutableList.forEach {
                println("/////////")
                println(it.dateLikeDateType)
                println(it.date)
                println(it.title)
                println(it.description)
                println(it.keywords)
            }

            showCorrectDate()
           // givenString_whenCustomFormat_thenLocalDateTimeCreated()
        }
    }
    // val result = client.newCall(request).await()
    //println("${result.code()}: ${result.message()}")
}

private fun givenString_whenCustomFormat_thenLocalDateTimeCreated(dateTime: String): LocalDateTime {
    val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")
    val localDateTime = LocalDateTime.parse(dateTime, pattern)
    //println("Date date")
   // println(localDateTime)
    return localDateTime
}

private fun showCorrectDate() {

    val secondApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss'Z'")
    val timestamp = 1565209665.toLong() // timestamp in Long


    val timestampAsDateString = java.time.format.DateTimeFormatter.ISO_INSTANT
        .format(java.time.Instant.ofEpochSecond(timestamp))

    println("parseTesting ${timestampAsDateString}") // prints 2019-08-07T20:27:45Z


    val date = LocalDate.parse(timestampAsDateString, secondApiFormat)

    println("parseTesting ${date.dayOfWeek.toString()}") // prints Wednesday
    println("parseTesting ${date.month.toString()}") // prints August
}