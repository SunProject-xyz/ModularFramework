package com.github.sunproject.org.modularframework.providers.modulemanager;


import com.github.sunproject.org.modularframework.events.ModularEventHandler;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public abstract class ModularModule {
	private static final HashMap<String, ModularModule> moduleList = new HashMap<>();
	private Class<?> moduleClassMain;
	private JSONObject moduleJsonMeta;
	private File moduleJarFile;
	private String moduleName, moduleVersion, author, webSiteUrl;
	private long targetApiLevel, minApiLevel, maxApiLevel;
	private boolean isEnabled;
	private ModularEventHandler preInitEvent;

	public ModularModule(String moduleName, long minApi) {
		
		if (!moduleName.isEmpty()) {
			this.setModuleName(moduleName);			
		} else throw new NullPointerException("moduleName cannot be null !");
		
		this.setMinApiLevel(minApi);

		// Add Plugin
		getModulesList().put(getModuleName(), this);
	}

	public void parseJsonMetaData() {
		if (!moduleJsonMeta.isEmpty()) {
//			this.moduleJarFile = new File(moduleClassMain.getProtectionDomain().getCodeSource().getLocation().getPath());
			this.targetApiLevel = (long) moduleJsonMeta.get("targetApiLevel");
			this.minApiLevel = (long) moduleJsonMeta.get("minApiLevel");
			this.maxApiLevel = (long) moduleJsonMeta.get("maxApiLevel");
			this.moduleName = (String) moduleJsonMeta.get("moduleName");
			this.moduleVersion = (String) moduleJsonMeta.get("moduleVersion");
			JSONObject authorInfo = (JSONObject) moduleJsonMeta.get("authorInfo");
			this.author = (String) authorInfo.get("authorName");
			this.webSiteUrl = (String) authorInfo.get("webSiteUrl");
		}
	}

	public JSONObject getModuleJsonMeta() {
		return moduleJsonMeta;
	}

	public void setModuleJsonMeta(InputStream moduleJsonMeta) {
		this.moduleJsonMeta = (JSONObject) JSONValue.parse(new InputStreamReader(moduleJsonMeta));
		parseJsonMetaData();

	}

	public void setModuleClassMain(Class<?> moduleClassMain) {
		this.moduleClassMain = moduleClassMain;
	}

	public void setModuleJarFile(File moduleJarFile) {
		this.moduleJarFile = moduleJarFile;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public void setModuleVersion(String moduleVersion) {
		this.moduleVersion = moduleVersion;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setWebSiteUrl(String webSiteUrl) {
		this.webSiteUrl = webSiteUrl;
	}

	public void setTargetApiLevel(long targetApiLevel) {
		this.targetApiLevel = targetApiLevel;
	}

	public void setMinApiLevel(long minApiLevel) {
		this.minApiLevel = minApiLevel;
	}

	public void setMaxApiLevel(long maxApiLevel) {
		this.maxApiLevel = maxApiLevel;
	}

	public static HashMap<String, ModularModule> getModulesList() {
		return moduleList;
	}

	public Class<?> getModuleClassMain() {
		return moduleClassMain;
	}

	public File getModuleJarFile() {
		return moduleJarFile;
	}

	public String getModuleName() {
		return moduleName;
	}

	public static int getModuleNumber() {
		return getModulesList().size();
	}

	public long getTargetApiLevel() {
		return targetApiLevel;
	}

	public long getMinApiLevel() {
		return minApiLevel;
	}

	public long getMaxApiLevel() {
		return maxApiLevel;
	}

	public String getModuleVersion() {
		return moduleVersion;
	}

	public String getAuthor() {
		return author;
	}

	public abstract void onEnable();

	public abstract void onDisable();

	public void setOnPreInit(ModularEventHandler event) {
		preInitEvent = event;
	}

	public String getWebSiteUrl() {
		return webSiteUrl;
	}

	public JSONObject getModuleMetaUrl() {
		return moduleJsonMeta;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}

	public void removeModule() throws FileNotFoundException {
		if (!getModuleJarFile().delete())
			throw new FileNotFoundException("file not deleted !");
		getModulesList().remove(getModuleName());
	}
	
	public static void registerModule(ModularModule module) {
		if (!getModulesList().containsValue(module)) {
			getModulesList().put(module.getModuleName(), module);
		}
	}
}








