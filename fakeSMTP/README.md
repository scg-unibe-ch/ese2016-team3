# FakeSMTP für Unit Tests

Falls in Unit Tests Mails verschickt werden, wird automatisch ein lokaler SMTP-Server verwendet. Dafür wird das Programm [fakeSMTP](https://nilhcem.github.io/FakeSMTP/) verwendet.

Das Programm ist ins Repository eingecheckt und muss nicht heruntergeladen werden.

Das Programm muss vor dem Ausführen von Unit-Tests gestartet und konfiguriert werden.

1. Starte das Programm `/fakeSMTP/fakeSMTP-2.0.jar`
2. Setze den *Listening Port* auf 2525 und klicke *Start server*

	![](./startup.png)

3. Gesendete Nachrichten werden unten im grossen weissen Bereich angezeigt und können per Doppelklick geöffnet werden.