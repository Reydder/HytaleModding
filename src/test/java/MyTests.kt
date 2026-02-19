import com.google.gson.Gson
import com.reydder.spawn.data.GameConfig
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
        val roundPath = Paths.get("").resolve("ZombiesGameConfig").resolve("GameConfig.json")

        val gameConfigs = Gson().fromJson<GameConfig>(Files.readString(roundPath), GameConfig::class.java)

        print(gameConfigs)

        assert(Files.exists(Paths.get("ZombiesGameConfig")))
        assert(gameConfigs.maps.size == 2)
    }

    @Test
    fun round_settings_modifier() {
        var round = 100

        var zombies = 15
        var maxSpawnPerTick = 3


        /**
         * Cada 5 rondas
         * - Se incrementa el numero de zombies
         * Cada 10
         * - Se incrementa el numero de zombies por tick con un maximo de 6
         */

        val zombiesModifierChunks = round / 5
        val maxSpawnPerTickChunks = round / 5

        zombies += (6 * zombiesModifierChunks).coerceAtMost(30)
        maxSpawnPerTick += (1 * maxSpawnPerTickChunks).coerceAtMost(7)


        print(((6 * zombiesModifierChunks) and 0b11111))

        assertEquals(45, zombies)
        assertEquals(10, maxSpawnPerTick)

    }

}