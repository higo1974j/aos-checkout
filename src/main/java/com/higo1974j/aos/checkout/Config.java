package com.higo1974j.aos.checkout;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Config {

  public long lastModified = -1;

  public Properties prop;

  public String filename;

  private Config() {
    prop = new Properties();
  }

  synchronized public void load(String filename) throws IOException {
    this.filename = filename;
    File file = new File(filename);
    load();
    lastModified = file.lastModified();
  }
  synchronized public void check() {
    File file = new File(filename);
    long tmpLastModified = file.lastModified();
    if(lastModified == tmpLastModified) {
      return;
    }
    log.info("uppdating");
    try {
      load();
      lastModified = tmpLastModified;
    } catch(IOException e) {
      log.warn("Config load error", e);
    }
  }

  protected void load() throws IOException {
    File file = new File(filename);
    try (FileReader fr = new FileReader(file)) {
      prop.load(fr);
    }
  }

  public String getProperty(String key) {
    check();
    return prop.getProperty(key);
  }

  private static Config instance = new Config();

  public static Config getInstance() {
    return instance;
  }
}