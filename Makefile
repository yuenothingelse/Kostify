JFX_VER ?= 25.0.1
JFX_LIB ?= $(HOME)/javafx-sdk-$(JFX_VER)/lib
SRC_DIR := src/main/java
RES_DIR := src/main/resources
OUT_DIR := out
LIB_DIR := lib
MAIN_CLASS := com.kostify.MainApp
MODULES := javafx.controls
JAVA_FILES := $(shell find $(SRC_DIR) -name "*.java")

.PHONY: help deps compile run db-up db-down clean

help:
	@echo "  make deps     -> Unduh MySQL Connector & JavaFX ($(JFX_VER))"
	@echo "  make compile  -> Kompilasi kode Java dan salin resources"
	@echo "  make run      -> Kompilasi dan jalankan aplikasi Kostify"
	@echo "  make db-up    -> Jalankan container MySQL Docker (kostify-mysql)"
	@echo "  make db-down  -> Hentikan container MySQL Docker"
	@echo "  make clean    -> Hapus folder hasil kompilasi (out)"

deps:
	@mkdir -p "$(JFX_LIB)" "$(LIB_DIR)"
	curl -fL -o "$(LIB_DIR)/mysql-connector-j-8.4.0.jar" "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.4.0/mysql-connector-j-8.4.0.jar"
	for mod in base graphics controls; do jar_name="javafx-$${mod}-$(JFX_VER)-mac-aarch64.jar"; curl -fL -o "$(JFX_LIB)/$$jar_name" "https://repo1.maven.org/maven2/org/openjfx/javafx-$${mod}/$(JFX_VER)/$$jar_name"; done

compile:
	@rm -rf $(OUT_DIR)
	@mkdir -p $(OUT_DIR)
	javac --module-path "$(JFX_LIB)" --add-modules $(MODULES) -cp "$(LIB_DIR)/*" -d $(OUT_DIR) $(JAVA_FILES)
	@if [ -d "$(RES_DIR)" ] && [ -n "$$(ls -A $(RES_DIR) 2>/dev/null)" ]; then cp -R $(RES_DIR)/* $(OUT_DIR)/; fi

run: compile
	java --module-path "$(JFX_LIB)" --add-modules $(MODULES) -cp "$(OUT_DIR):$(LIB_DIR)/*" $(MAIN_CLASS)

db-up:
	@docker start kostify-mysql 2>/dev/null || docker run -d --name kostify-mysql -e MYSQL_ROOT_PASSWORD=rootpass -e MYSQL_DATABASE=kostify_db -p 3306:3306 -v kostify_mysql_data:/var/lib/mysql mysql:8.0

db-down:
	docker stop kostify-mysql

clean:
	rm -rf $(OUT_DIR)