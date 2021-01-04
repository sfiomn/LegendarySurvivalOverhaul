package icey.survivaloverhaul.util.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import icey.survivaloverhaul.Main;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

// Code taken and adapted from Serene Seasons, copyright the Biomes o' Plenty Team. All rights reserved
// https://github.com/Glitchfiend/SereneSeasons/blob/SS-1.16.x-4.x.x/src/main/java/sereneseasons/util/config/JsonUtil.java

public class JsonUtil
{
	public static final Gson SERIALIZER = new GsonBuilder().setPrettyPrinting().create();

	@SuppressWarnings("unchecked")
	public static <T> T getOrCreateConfigFile(File configDir, String configName, T defaults, Type type)
	{
		File configFile = new File(configDir, configName);
		
		if (!configFile.exists())
		{
			writeFile(configFile, defaults);
		}
		
		try
		{
			return (T) SERIALIZER.fromJson(FileUtils.readFileToString(configFile, Charset.defaultCharset()), type);
		}
		catch (Exception e)
		{
			Main.LOGGER.error("Error parsing config from .json: " + configFile.toString(), e);
		}
		
		return null;
	}
	
	protected static boolean writeFile(File outputFile, Object obj)
	{
		try
		{
			FileUtils.write(outputFile, SERIALIZER.toJson(obj), Charset.defaultCharset());
			return true;
		}
		catch (Exception e)
		{
			Main.LOGGER.error("Error writing config file" + outputFile.getAbsolutePath() + ": " + e.getMessage());
			return false;
		}
	}
}
