import com.google.gson.Gson
import com.reydder.spawn.data.Round
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectory
import kotlin.test.assertEquals


class MyTests {

    @Test
    fun test0() {
        val roundPath = Paths.get("").resolve("Rounds").resolve("Round.json")

        val round =  Gson().fromJson<Round>(Files.readString(roundPath), Round::class.java)

        //print(Files.createDirectory(Paths.get("").resolve("Rounds")))


        print(round)

        assert(Files.exists(Paths.get("Rounds")))
        assert(round.round == 1)
    }

}