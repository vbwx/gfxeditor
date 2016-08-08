dist/build: $(wildcard src/gfxeditor/*.java) $(wildcard src/gfxeditor/*/*.java)
	[ -d .tmp ] || mkdir .tmp
	if javac -d .tmp $^; then               \
		[ -f $@ ] || echo 0 > $@;           \
		build=`awk '{ print $$1 + 1 }' $@`; \
		echo $$build > $@;                  \
	fi

dist/GfxEditor.jar: src/Manifest dist/build
	jar cfm $@ $< res -C .tmp gfxeditor

docs: $(wildcard src/gfxeditor/*.java) $(wildcard src/gfxeditor/*/*.java)
	javadoc -d docs -sourcepath src -subpackages gfxeditor && touch docs

.PHONY: all
all: dist/GfxEditor.jar docs

.PHONY: clean
clean:
	rm -rf .tmp
