# MineFectedSpawn
It's a Minecraft plugin that lets you create some spawn points in the world and then teleport to them whenever you want. It saves the data in a JSON.

## Commands
```
/mfspawn create %locName%
/mfspawn remove %locName%
/mfspawn spawn %locName%
/mfspawn spawn %locName% %player%
/mfspawn spawn
/mfspawn list
```

# Langs
``` YAML
  spawnpoint-created: '&eSe ha creado el spawnpoint &a<SPName>'
  spawnpoint-removed: '&cSe ha eliminado el spawnpoint &a<SPName>'
  player-teleported: '&eHas despertado en &a<SPName>'
  spawnpoint-title: '&4Te has despertado.'
  spawnpoint-subtitle: '&7Sobrevive a toda costa!'
  spawnpoint-not-found: '&cNo se ha encontrado el spawnpoint &a<SPName>'
  spawnpoint-name-error: '&cDebes especificar un nombre para el spawnpoint!'
  spawnpoint-creation-error: '&cError creando el spawnpoint &a<SPName>'
  spawnpoint-remove-error: '&cError eliminando el spawnpoint &a<SPName>'
  spawnpoint-already-exists: '&a<SPName> &cya existe, intenta con otro nombre.'
  spawnpoint-no-permission: '&cNo cuentas con los permisos necesarios para despertar.'
```

# Permissions
``` YAML
Permissions:
  spawnpoint-user: mfspawn.user
  spawnpoint-staff: mfspawn.staff
```
