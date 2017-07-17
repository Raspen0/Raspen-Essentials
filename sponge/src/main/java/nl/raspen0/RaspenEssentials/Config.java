package nl.raspen0.RaspenEssentials;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class Config {
    public final static TypeToken<Config> type = TypeToken.of(Config.class);
    @Setting public String motd;
    @Setting public List<String> langs = ImmutableList.of();
    @Setting public String localeDetectMode;
 //   @Setting public commands commands = new commands();
 //   @ConfigSerializable
 //   public static class commands  {
 //       @Setting public Boolean heal;
 //   }

}
