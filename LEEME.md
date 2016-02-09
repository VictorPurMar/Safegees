LEEME
======

EL PROYECTO SAFEGEES
====================
Safegees surge de la combinación de ideas y esfuerzos de en el marco de la Peacehack Barcelona 2015, evento en el cual obtuvo el primer premio del jurado. Es una idea del grupo conformado por Álvaro Abella Bascarán, Guillem Hernandez, Gerard Martí Tarragó y Víctor Purcallas Marchesi, basada en una sugerencia más amplia de la organización del Peacehack.
Safegees conforma conjunto de herramientas que permiten a un usuario que inicia un tránsito en condiciones adversas (categoría en la que encajan los refugiados) compartir información de modo seguro con su círculo de contactos y organizaciones reconocidas (con especial énfasis de ONGs especializadas).

Enlaces de interés
==================
Presentación del proyecto Safegees: https://docs.google.com/presentation/d/15YGZeQ_a8iLByrgpM-spi6kotXm6KE4Mvh8K8eNdKZc/edit#slide=id.p9
Más sobre Safegees (vídeos, imágenes): http://www.domestika.org/es/projects/220344-safegees

LA APP ANDROID
===============

La app de Android está siendo desarrollada por Victor Purcallas Marchesi <vpurcallas@gmail.com>
Tiene licencia GNU General Public License V3
Copyright (C) 2016  Victor Purcallas <vpurcallas@gmail.com>

Actualmente se encuentra en fase alpha en Google Play. Se puede descargar la app en desarrollo en:
https://play.google.com/store/apps/details?id=org.safegees.safegees

La aplicación será tanto web como nativa para Android. La app web, es sencilla de realizar una vez implementados los servicios y el servidor, y la necesaria web para organizaciones.

· Seguridad Este tipo de aplicación debe ser capaz de garantizar la seguridad de una información trascendental, una ruptura de la seguridad podría afectar a la seguridad de las personas.
· Utilización de hardware del terminal como puede ser el GPS, la cámara y demás accesorios. Esta app se basa en la utilización del GPS del móvil para geolocalizar con exactitud al usuario.
· Conexión: la base de esta app es que pueda ser utilizada también sin conexión, aportando al información al usuario que ha sido previamente guardada en el dispositivo. Tanto el acceso a las noticias, como el mapa con sus puntos y la posición de los contactos debería ser accesible en todo momento por el usuario
· Rendimiento: es una app que debe ser rápida de usar, minimizando el consumo de batería. No únicamente por la experiencia de usuario (que se vería ampliamente mejorada) sino que se ha planteado la posibilidad de que durante el tránsito los usuarios no dispondrán en muchos casos de posibilidad de cargar su dispositivo, con lo la velocidad de uso parece ser un aspecto a tener en cuenta.

La aplicación comenzará siempre con un registro de usuario con nombre de usuario y clave, así como un correo de contacto.
Una vez se efectúe el registro, tratando de simplificar los procesos innecesarios, el usuario directamente verá un mapa, con su posición (por ip o gps), así como todos los puntos de interés dispuestos por las organizaciones. 
Para las apps móviles se ha planteado utilizar los servicios de mapa de los que disponen ambos dispositivos. Los servicios de google para Android y de Apple Maps para IOS. A su vez se ha de profundizar en el modo para guardar mapas básicos que puedan ser visibles también sin conexión. Para ello se ha contemplado la posibilidad de utilizar los servicios de localización del dispositivo junto con mapas descargables de Open Street Maps, que son libres y también y no chocan con el trasfondo ético del proyecto.
El usuario dispondrá de un botón de actualizar. Clickando se realizan los push y get al servidor, por lo tanto requiere algún tipo de conexión a internet. Enviará su información, posición así como su lista de contactos y recibirá la información genérica de las organizaciones así como la info de sus contactos.
Un mismo dispositivo puede tener alojadas varias cuentas siendo sensible con la situación de recursos de los refugiados. Al realizar una actualización se actualizará la info de todos los usuarios. (Los no principales en segundo plano). Por lo que se podrá realizar logins de cuentas alojadas incluso sin conexión Siendo necesario actualizar únicamente una vez y minimizando el consumo innecesario de recursos y conexión. Este aspecto atiende a las condiciones precarias en un gran porcentaje de usuarios sin acceso a móvil.


FUNCIONALIDADES GENERALES DEL PROYECTO SAFEGEES:
================================================

Recibir información de las ONGs importante para su supervivencia en la huida del conflicto, a través de la localización de puntos clave sobre el mapa, editados por estas ONGs.
Geolocalizar a las personas que conoce durante el trayecto así como a su círculo cercano.

Funcionalidades para las organizaciones:
Crear un canal de comunicación con los refugiados, para aportar consejos, sucesos y en general un conocimiento valioso para los refugiados en tránsito.
Estudiar cómo funcionan los flujos de refugiados (de modo anónimo y seguro).

Tanto la app como la web tratará de ser lo más sencillas tanto funcional como visualmente, para facilitar el uso a todos los usuarios (también tecnófobos)




