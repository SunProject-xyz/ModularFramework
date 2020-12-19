package com.github.sunproject.org.modularframework.internal.providers.modulemanager;

import com.github.sunproject.org.modularframework.internal.init.ModularInit;
import com.github.sunproject.org.modularframework.internal.console.ModularLog;
import com.github.sunproject.org.modularframework.internal.utils.Pluralize;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;

/**
 * @since 1.0
 * @author sundev79 (sundev79.sunproject@gmail.com)
 * Modular module manager.
 */

public class ModularModuleManager {

	private ModularLog console = ModularInit.getConsole();

	public ModularModuleManager() {
		console.log("Starting up Module Manager ...");
		console.log("Detected " + "(" + ModularModule.getModuleNumber() + ") "
				+ Pluralize.pluralizeWord("Module", ModularModule.getModuleNumber()) + " !");
	}

	public boolean reloadPluginList() {
		System.out.println("Reloading Module List ...");
		try {
			ModularInit.getModuleLoader().startIndexation();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void shutdown() {
		console.log("Stopping Module Manager ...");
		console.log("Stopping ALL Modules " + "(" + ModularModule.getModuleNumber() + ")" + "...");

		if (!ModularModule.getModulesList().isEmpty()) {
			Iterator<Map.Entry<String, ModularModule>> it = ModularModule.getModulesList().entrySet().iterator();

			while (it.hasNext()) {
				Map.Entry<String, ModularModule> entry = it.next();

				try {
					disableModule(getModuleByName(entry.getValue().getModuleName()));
					it.remove();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
		console.log("Done !");
	}

	public void enableModule(ModularModule module) throws NoSuchMethodException {
		if (!module.isEnabled()) {
			console.log("Enabling Module " + module.getModuleName() + "...");
			Thread modRunner = new Thread(() -> {
				module.onEnable();
			});

			modRunner.setName("Module_" + module.getModuleName());
			modRunner.start();
			module.setEnabled(true);
		}
	}

	public void disableModule(ModularModule module) throws NoSuchMethodException {
		if (module.isEnabled()) {
			console.log("Disabling Module " + module.getModuleName() + "...");
			Thread modRunner = new Thread(() -> {
				module.onDisable();
			});

			modRunner.setName("Module_" + module.getModuleName() + " Stopping ...");
			modRunner.start();
			module.setEnabled(false);
		}
	}

	public void deleteModule(ModularModule module) {
		console.log("Deleting Module" + module.getModuleName() + " ...");
		try {
			disableModule(module);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		try {
			module.removeModule();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public ModularModule getModuleByName(String moduleName) {

		if (!ModularModule.getModulesList().isEmpty())
			return ModularModule.getModulesList().get(moduleName);

		return null;
	}
}