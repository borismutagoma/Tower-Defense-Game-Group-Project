### Makefile Optimized for Livrable6 ###

# --------- VARIABLES ---------

SRC_DIR = src
TEST_DIR = test
CLASS_DIR = classes
DOCS_DIR = docs
JAR_DIR = jar

JAVAC = javac
JAVA = java
JAR = jar
JAVADOC = javadoc

JUNIT = junit-console.jar

TDGInteractiveA = towerdefense.livrables.TDGInteractiveA
TDGInteractiveB = towerdefense.livrables.TDGInteractiveB
TDGRandomA = towerdefense.livrables.TDGRandomA
TDGRandomB = towerdefense.livrables.TDGRandomB
ARGS =


BUILD_SENTINEL = $(CLASS_DIR)/.build_success
TEST_SENTINEL = $(CLASS_DIR)/.tests_compiled_success

SOURCES = $(shell find $(SRC_DIR) -name "*.java")
TEST_SOURCES = $(shell find $(TEST_DIR) -name "*.java")

# --------- TARGETS ---------

.PHONY: all clean runtests runTDG_I_A runTDG_I_B runTDG_R_A runTDG_R_B directories classes jar docs tests

all: jar docs

directories:
	@mkdir -p $(CLASS_DIR) $(DOCS_DIR) $(JAR_DIR)


$(BUILD_SENTINEL): $(SOURCES) | directories
	$(JAVAC) -sourcepath $(SRC_DIR) $(SOURCES) -d $(CLASS_DIR)
	@touch $(BUILD_SENTINEL)


classes: $(BUILD_SENTINEL)


$(DOCS_DIR)/index.html: $(SOURCES) | directories
	$(JAVADOC) -sourcepath $(SRC_DIR) -subpackages towerdefense -d $(DOCS_DIR)
	@touch $(DOCS_DIR)/index.html

docs: $(DOCS_DIR)/index.html


$(JAR_DIR)/towerdefense-a-interactive.jar: $(BUILD_SENTINEL)
	$(JAR) cvfe $(JAR_DIR)/towerdefense-a-interactive.jar $(TDGInteractiveA) -C $(CLASS_DIR) .

$(JAR_DIR)/towerdefense-b-interactive.jar: $(BUILD_SENTINEL)
	$(JAR) cvfe $(JAR_DIR)/towerdefense-b-interactive.jar $(TDGInteractiveB) -C $(CLASS_DIR) .

$(JAR_DIR)/towerdefense-a-random.jar: $(BUILD_SENTINEL)
	$(JAR) cvfe $(JAR_DIR)/towerdefense-a-random.jar $(TDGRandomA) -C $(CLASS_DIR) .

$(JAR_DIR)/towerdefense-b-random.jar: $(BUILD_SENTINEL)
	$(JAR) cvfe $(JAR_DIR)/towerdefense-b-random.jar $(TDGRandomB) -C $(CLASS_DIR) .

jar: $(JAR_DIR)/towerdefense-a-interactive.jar $(JAR_DIR)/towerdefense-b-interactive.jar $(JAR_DIR)/towerdefense-a-random.jar $(JAR_DIR)/towerdefense-b-random.jar


$(TEST_SENTINEL): $(BUILD_SENTINEL) $(TEST_SOURCES)
	$(JAVAC) -classpath $(JUNIT):$(CLASS_DIR) $(TEST_SOURCES) -d $(CLASS_DIR)
	@touch $(TEST_SENTINEL)

tests: $(TEST_SENTINEL)

runtests: tests
	$(JAVA) -jar $(JUNIT) -classpath $(CLASS_DIR) -scan-classpath

runTDG_I_A : $(JAR_DIR)/towerdefense-a-interactive.jar
	$(JAVA) -jar $(JAR_DIR)/towerdefense-a-interactive.jar $(ARGS)

runTDG_I_B : $(JAR_DIR)/towerdefense-b-interactive.jar
	$(JAVA) -jar $(JAR_DIR)/towerdefense-b-interactive.jar $(ARGS)

runTDG_R_A : $(JAR_DIR)/towerdefense-a-random.jar
	$(JAVA) -jar $(JAR_DIR)/towerdefense-a-random.jar $(ARGS)

runTDG_R_B : $(JAR_DIR)/towerdefense-b-random.jar
	$(JAVA) -jar $(JAR_DIR)/towerdefense-b-random.jar $(ARGS)


clean:
	rm -rf $(CLASS_DIR) $(DOCS_DIR) $(JAR_DIR)