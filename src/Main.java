import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import com.jme3.water.SimpleWaterProcessor;

/**
 * Clase principal que lanza la aplicación Juego PMDM - T3
 * relativa a las actividades propuestas del Tema 3 de PMDM.
 * 
 * @author Odei
 * @version 15.01.2016
 */
public class Main extends SimpleApplication {
    /**
     * Variable usada para reproducir una canción en bucle al lanzar el juego.
     */
    private AudioNode cancion;
    
    /**
     * Variable usada para reproducir un sonido al lanzar la bola.
     */
    private AudioNode click;
    
    /**
     * Variable usada para reproducir un sonido al marcar un gol.
     */
    private AudioNode gol;
    
    /**
     * Variable usada para mostar el número de bolas restantes a utilizar.
     */
    private BitmapText bolasTxt;
    
    /**
     * Variable usada para mostar el menú utilizado durante el juego.
     */
    private BitmapText menuTxt;
    
    /**
     * Variable usada para mostar el número de goles marcados durante el juego.
     */
    private BitmapText golesTxt;
    
    /**
     * Variable usada para mostrar una mirilla en el centro durante el juego.
     */
    private BitmapText puntero;
    
    /**
     * Variable usada para comprobar la activación o no del debug.
     */
    private boolean debug;
    
    /**
     * Variable usada para comprobar la activación o no del estado.
     */
    private boolean estado;
    
    /**
     * Variable usada para comprobar la activación o no de la gravedad.
     */
    private boolean gravedad;
    
    /**
     * Variable usada para comprobar la activación o no del menú.
     */
    private boolean menu;
    
    /**
     * Variable usada cambiar la dirección de movimiento de elementos móviles.
     */
    private boolean swD;
    
    /**
     * Variable usada para emular el Motor de Físicas de la escena.
     */
    private BulletAppState bulletAppState;
    
    /**
     * Variable final real usada para almacenar el ancho del mundo.
     */
    private final float anchoM = 28F;
    
    /**
     * Variable final real usada para almacenar el largo del mundo.
     */
    private final float largoM = 50F;
    
    /**
     * Variable final real usada para almacenar el ancho de la portería.
     */
    private final float anchoP = 7.2F;
    
    /**
     * Variable final real usada para almacenar el largo de la portería.
     */
    private final float altoP = 2.4F;
    
    /**
     * Variable entera usada para contar el número de bolas a lanzar.
     */
    private int numBolas;
    
    /**
     * Variable entera usada para contar el número de goles marcados.
     */
    private int numGoles;
    
    /**
     * Variable usada para cargar el material usado para crear la bola.
     */
    private Material bola_mat;
    
    /**
     * Variable usada para cargar el material usado para crear los cilindros.
     */
    private Material cilindro_mat;
    
    /**
     * Variable usada para cargar el material usado para crear los prismas.
     */
    private Material prisma_mat;
    
    /**
     * Variable usada para cargar el material usado para crear las explosiones.
     */
    private Material explosion_mat;
    
    /**
     * Variable usada para cargar el material usado para crear la pared.
     */
    private Material pared_mat;
        
    /**
     * Variable usada para cargar el material usado para crear el suelo.
     */
    private Material suelo_mat;
    
    /**
     * Variable usada para cargar el material usado para crear la red.
     */
    private Material red_mat;
    
    /**
     * Nodo usado para almacenar el portero utilizado durante el juego.
     */
    private Node porteroN;
    
    /**
     * Nodo usado para almacenar los defensas utilizados durante el juego.
     */
    private Node defensaN;
    
    /**
     * Nodo usado para almacenar las bolas utilizadas durante el juego.
     */
    private Node bolaN;
    
    /**
     * Variable usada para generar explosiones cuando se marca un gol.
     */
    private ParticleEmitter efecto;
    
    /**
     * Variable de coordenadas usada para almacenar la posición de la cámara.
     */
    private Vector3f corAux;
    
    /**
     * Variable de coordenadas usada para almacenar la posición de la bola.
     */
    private Vector3f corBola;
    
    /**
     * Variable final de coordenadas usada para almacenar la posición de la luz.
     */
    private final Vector3f corLuz = new Vector3f(-1.1F, -2F, -1F);
    
    /**
     * Genera y ejecuta una instancia de la aplicación cambiando el título e
     * imagen por defecto creando una nueva configuración para el programa.
     * 
     * @param args String[]: argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        AppSettings as = new AppSettings(true);                                 // Creamos una nueva configuración
        as.setTitle("Juego PMDM - T3");                                         // asignandole un nuevo nombre para la ventana
        as.setSettingsDialogImage("Textures/intro.jpg");                        // y la imagen a mostrar para la misma
        Main app = new Main();                                                  // Creamos la aplicación,
        app.setSettings(as);                                                    // le asignamos la configuración creada y la arrancamos
        app.start();
    }
    
    /**
     * Método sobrecargado usado para inicializar y cargar los elementos 
     * utilizados durante la partida.
     */
    @Override
    public void simpleInitApp() {
        numBolas = 5;                                                           // Establecemos el número de bolas a lanzar
        numGoles = 0;                                                           // Establecemos el número de goles realizados
        gravedad = swD = true;                                                  // Activamos gravedad y sw usado para controlar el movimiento de los elementos móviles
        menu = debug = estado = false;                                          // Desactivamos el menú, estado y debug
        activarMotorFisicas();                                                  // Activamos el motor de físicas
        crearMateriales();                                                      // Creamos los materiales necearios para las geometrías utilizadas
        crearSuelo();                                                           // Creamos suelo y pared
        crearPared();
        crearCielo();                                                           // Creamos cielo y mar
        crearMar();
        crearPorteria();                                                        // Creamos portería y elementos Npc y geométricos a batir
        crearNpc();
        crearLuz();                                                             // Creamos efecto de luz para visualizar los objetos
        cargarTexto();                                                          // Escribimos los mensajes a mostrar por pantalla y cargamos las teclas configuradas
        cargarTeclas();
        cargarAudio();                                                          // Cargamos los audios usados durante la partida
        cargarExplosion();                                                      // Cargamos el efecto explosión creado para celebrar los goles
        setDisplayStatView(false);
        bolaN = new Node("Bolas");                                              // Inicializamos nodo de bolas bolaN y lo agregamos al grafo
        rootNode.attachChild(bolaN);
        cam.setLocation(new Vector3f(0, 2f, 20f));                              // Colocamos la cámara en una posición adecuada para ver la superficie del suelo
        flyCam.setMoveSpeed(5);                                                 // Establecemos la velocidad de movimiento de la cámara
    }
    
    /**
     * Método usado para activar el Motor de Físicas usado durante el Juego.
     */
    protected void activarMotorFisicas(){
        bulletAppState = new BulletAppState();                                  // Creamos un simulador y
        stateManager.attach(bulletAppState);                                    // acoplamos al bucle interno del motor de juegos
    }
    
   /**
     * Método usado para crear los diferentes materiales que utilizaremos para 
     * construir los objetos que poblaran nuestra escena.
     */
    protected void crearMateriales() {      
        suelo_mat = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");                           // Creamos el material para el suelo
        TextureKey suelo = new TextureKey("Textures/campo.jpg");                // y le asignamos la TextureKey a usar a partir de la imagen campo.jpg
        suelo.setGenerateMips(true);
        Texture texturaSuelo = assetManager.loadTexture(suelo);                 // cargamos la textura para enlazarla al suelo
        texturaSuelo.setWrap(WrapMode.Clamp);                                   // estableciendo que se estire a lo anchoM del tamaño de este
        suelo_mat.setTexture("ColorMap", texturaSuelo);
        
        pared_mat = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");                           // Material para la pared  
        TextureKey pared = new TextureKey(
                "Textures/cartel.jpg");
        pared.setGenerateMips(true);
        Texture texturaPared = assetManager.loadTexture(pared);
        texturaPared.setWrap(WrapMode.Clamp);
        pared_mat.setTexture("ColorMap", texturaPared);
        
        red_mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");                           // Material para la red  
        TextureKey red = new TextureKey("Textures/red.png");
        red.setGenerateMips(true);
        Texture texturaRed = assetManager.loadTexture(red);
        texturaRed.setWrap(WrapMode.Repeat);                                    // establecemos que se repita la textura para simular el efecto deseado
        red_mat.setTexture("ColorMap", texturaRed);
        red_mat.getAdditionalRenderState().setBlendMode(                        // y agregamos la capa como transparente para poder ver sobre ella
                RenderState.BlendMode.Alpha);
         
        explosion_mat = new Material(assetManager, 
                "Common/MatDefs/Misc/Particle.j3md");                           // Creamos el material de la explosión
        explosion_mat.setTexture("Texture", assetManager.loadTexture(
                "Effects/Explosion/flame.png"));
        explosion_mat.setBoolean("PointSprite", true);
        
        bola_mat = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");                           // Creamos el material de la bola
        TextureKey bola = new TextureKey("Textures/balon.jpg");
        bola.setGenerateMips(true);
        Texture texturaBola = assetManager.loadTexture(bola);
        texturaBola.setWrap(WrapMode.Clamp);
        bola_mat.setTexture("ColorMap", texturaBola);        
        
        cilindro_mat = new Material(assetManager, 
                "Common/MatDefs/Light/Lighting.j3md");                          // Creamos el material para los cilindros a partir de una base iluminada y la hacemos de color blanco reflectante
        cilindro_mat.setBoolean("UseMaterialColors", true);
        cilindro_mat.setColor("Specular", ColorRGBA.White);
        cilindro_mat.setColor("Ambient", ColorRGBA.White);
        cilindro_mat.setColor("Diffuse", ColorRGBA.White);

        prisma_mat = cilindro_mat.clone();                                      // Creamos el material del prisma clonando el de los cilindros y modificando los colores
        prisma_mat.setColor("Specular", ColorRGBA.Red);
        prisma_mat.setColor("Ambient", ColorRGBA.Red);
        prisma_mat.setColor("Diffuse", ColorRGBA.Red);
    }
    
    /**
     * Método usado para crear el suelo utilizado durante el juego.
     */
    protected void crearSuelo() {
        Box suelo = new Box(anchoM, 0.1F, largoM);                              // Creamos una forma de caja de 28 metros de anchoM, 10cm de alto y 50 metros de largoM
        Geometry suelo_geo = new Geometry("Suelo", suelo);                      // Creamos un spatial de tipo geometría para asociarlo al suelo
        suelo_geo.setMaterial(suelo_mat);                                       // Asignamos el material correspondiente para visualizar el objeto
        suelo_geo.setLocalTranslation(0, -0.1f, 0);                             // Bajamos el suelo 10cm para que el origen esté por encima.
        rootNode.attachChild(suelo_geo);                                        // Añadimos el suelo en el grafo de escena
        suelo_geo.setShadowMode(RenderQueue.ShadowMode.Receive);                // establecemos que sólo reciba sombra
        RigidBodyControl suelo_fis = new RigidBodyControl(0.0f);                // Asociamos el objeto de control a la geometría de la pared
        suelo_geo.addControl(suelo_fis);
        bulletAppState.getPhysicsSpace().add(suelo_fis);                        // y añadimos el objeto de control al motor de físicas
    }

    /**
     * Método usado para crear una pared (a modo de cartel) en el juego.
     */
    protected void crearPared() {
        Box pared = new Box(9F, 5F, 0.2F);                                      // Creamos una forma de caja de 9 metros de anchoM, 5 metros de alto y 20cm de grosor
        Geometry pared_geo = new Geometry("Pared", pared);                      // Creamos un spatial de tipo geometría para asociarlo a la pared
        pared_geo.setLocalTranslation(0, 6F, -largoM);                          // Desplazamos la pared sobre el suelo hacia el fondo de la escena
        pared_geo.setMaterial(pared_mat);                                       // le asignamos el material correspondiente
        rootNode.attachChild(pared_geo);                                        // le asignamos la pared al grafo de escena        
        pared_geo.setShadowMode(RenderQueue.ShadowMode.Receive);               // establecemos que sólo reciba sombra
        RigidBodyControl pared_fis = new RigidBodyControl(0f);                  // asociamos el objeto de control a la geometría
        pared_geo.addControl(pared_fis);
        bulletAppState.getPhysicsSpace().add(pared_fis);                        // y añadimos el objeto al motor de físicas
    }

    /**
     * Método usado para generar el cielo mostrado durante el juego.
     */
    protected void crearCielo(){
        Spatial cielo = SkyFactory.createSky(assetManager,                      // Creamos el objeto Spatial SkyFactory
            assetManager.loadTexture("Textures/lago_es.jpg"),                   // y le asignamos las imágenes a usar 
            assetManager.loadTexture("Textures/lago_oe.jpg"),                   // para las coordenadas este, oeste, sur, norte, arriba y abajo
            assetManager.loadTexture("Textures/lago_su.jpg"), 
            assetManager.loadTexture("Textures/lago_no.jpg"), 
            assetManager.loadTexture("Textures/lago_ar.jpg"), 
            assetManager.loadTexture("Textures/lago_ab.jpg"),Vector3f.UNIT_XYZ);
        rootNode.attachChild(cielo);                                            // y lo agregamos al grafo
    }
    
    /**
     * Método usado para generar el mar mostrado durante la ejecución del juego.
     */
    protected void crearMar(){
        SimpleWaterProcessor swp = new SimpleWaterProcessor(assetManager);      // Creamos el objeto SimpleWaterProcessor
        swp.setReflectionScene(getRootNode());                                  // y le decimos que refleje los elementos del nodo raíz
        viewPort.addProcessor(swp);
        swp.setLightPosition(corLuz.normalizeLocal());                          // establecemos la luz posicional que afecta al mar
        Spatial agua = (Spatial) assetManager.loadModel(                        // cargamos el modelo usado para mostrar el efecto del agua
                "Models/WaterTest/WaterTest.mesh.xml");
        agua.setMaterial(swp.getMaterial());
        agua.setLocalScale(300);                                                // lo escalamos para que sea visible a lo lejos 
        agua.setLocalTranslation(0, -2, 0);                                     // lo posicionamos en el centro bajo el campo 
        rootNode.attachChild(agua);                                             // y lo agregamos al nodo
    }
    
    /**
     * Método usado para generar los elementos que formarán la estructura 
     * de la portería y la red que la recubre por detrás y encima.
     */
    protected void crearPorteria() {
        float[] cordXP1 = {-anchoP/2, 0F, anchoP/2};
        float[] cordYP1 = {1.05F, altoP, 1.05F};
        float[] cordZT1 = {altoP+0.1F, anchoP+0.1F, altoP+0.1F};                // Creamos varios vectores de coordenadas para reducir el código
        float[] cordXI1 = {FastMath.HALF_PI, 0F, FastMath.HALF_PI};                 
        float[] cordYI1 = {0F, FastMath.HALF_PI, 0F};
        for (int x = 0; x < cordXP1.length; x++){
            Cylinder palo = new Cylinder(10, 15, 0.12F, cordZT1[x], true);      // Creamos varios cilindros para establecer los palos de la porteria
            Geometry palo_geo = new Geometry("poste", palo);                    // Creamos una geometría y le asignamos el cilindro creado
            palo_geo.setLocalTranslation(cordXP1[x], cordYP1[x], -largoM+5.3F); // lo posicionamos usando coordenadas x, y, z, a partir de centro de la escena 
            palo_geo.setLocalRotation(new Quaternion().fromAngles(              // indicamos la inclinación para dicho elemento
                    cordXI1[x],cordYI1[x], 0));
            palo_geo.setMaterial(cilindro_mat);                                 // le asignamos el material usado 
            palo_geo.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);     // establecemos que proyecte y reciba sombras
            RigidBodyControl palo_fis = new RigidBodyControl(30F);              // le creamos su física correspondiente estableciendo un peso de 30 kg.
            palo_geo.addControl(palo_fis);
            palo_fis.setKinematic(true);                                        // lo creamos como cinemático para que no se vea afectado por las demás físicas pero pueda afectar a las mismas
            bulletAppState.getPhysicsSpace().add(palo_fis); 
            rootNode.attachChild(palo_geo);                                     // y agregamos su física a la escena y el elemento al nodo padre
        }
       
        float[] cordXT2 = {anchoP/2-0.1F, anchoP/2-0.1F, 1.32F, 1.32F};
        float[] cordYT2 = {altoP/2, 0.1F, altoP/2, altoP/2};
        float[] cordZT2 = {0.1F, 1.3F, 0.1F, 0.1F};
        float[] cordXP2 = {0F, 0F, -3.6F, 3.6F};
        float[] cordYP2 = {altoP/2, altoP, altoP/2, altoP/2};
        float[] cordZP2 = {-largoM+2.5F, -largoM+3.9F, -largoM+3.9F, -largoM+3.9F};
        float[] cordYI2 = {0F, 0F, FastMath.HALF_PI, FastMath.HALF_PI};
        for (int x = 0; x < cordXT2.length; x++){
            Box red = new Box(cordXT2[x], cordYT2[x], cordZT2[x]);              // Hacemos lo mismo para los elementos que recubren los palos en forma de red
            Geometry red_geo = new Geometry("Red", red);  
            red_geo.setMaterial(red_mat);        
            red_geo.setLocalTranslation(cordXP2[x], cordYP2[x], cordZP2[x]); 
            red_geo.setLocalRotation(new Quaternion().fromAngles(0,cordYI2[x],0));
            red_geo.setQueueBucket(RenderQueue.Bucket.Transparent);
            RigidBodyControl red_fis = new RigidBodyControl(0.0f);      
            red_geo.addControl(red_fis);
            bulletAppState.getPhysicsSpace().add(red_fis);
            rootNode.attachChild(red_geo);   
        }
    }
    
    /**
     * Método usado para generar y agregar los elementos Npc a la escena,
     * generando un portero, dos defensas móviles y cinco figuras estáticas.
     */
    protected void crearNpc(){
        porteroN =(Node)assetManager.loadModel("Models/Sinbad/Sinbad.mesh.xml");// Creamos un nodo a partir del modelo cargado Sinbad
        porteroN.setLocalTranslation(0F, 1F, -largoM+6F);                       // lo posicionamos delante de la portería del campo
        porteroN.setLocalScale(0.20F);                                          // lo escalamos para que no sea excesivamente grande
        RigidBodyControl portero_fis = new RigidBodyControl(40F);               // le agregamos su física generando un objeto de 40 kilos
        porteroN.addControl(portero_fis);                                       // lo establecemos como cinemático para que no se vea afectado 
        portero_fis.setKinematic(true);                                         // por las demás físicas pero pueda afectar a las mismas
        porteroN.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);          // establecemos que proyecte y reciba sombras
        bulletAppState.getPhysicsSpace().add(portero_fis);                      // agregamos su física al mundo
        rootNode.attachChild(porteroN);                                         // y agregamos de igual modo el nodo creado del mismo
        
        defensaN = new Node("Defensas");
        float[] coorDNX = {5F, -5F};                                            // Hacemos lo mismo creando varios defensas móviles a partir del módelo Oto
        for (int x = 0; x < coorDNX.length; x++){
            Node defensa=(Node)assetManager.loadModel("Models/Oto/Oto.mesh.xml");
            defensa.setLocalTranslation(coorDNX[x], 0.94F, -20F);  
            defensa.setLocalScale(0.20F);
            RigidBodyControl defensa_fis = new RigidBodyControl(40F);
            defensa.addControl(defensa_fis);
            defensa_fis.setKinematic(true);
            defensa.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
            bulletAppState.getPhysicsSpace().add(defensa_fis);
            defensaN.attachChild(defensa);
            rootNode.attachChild(defensaN);
        }
        
        Box figura = new Box(0.75F, 1.5F, 0.2F);                                // Hacemos lo mismo creando varios figuras geométricas estáticas
        float[] coorXF1 = {0F, 10F, -10F};
        for (int x = 0; x < coorXF1.length; x++){
            Geometry figura_geo = new Geometry("Figura", figura);                
            figura_geo.setLocalTranslation(coorXF1[x], 1.5F, -10F);                           
            figura_geo.setMaterial(cilindro_mat);                                                                     
            RigidBodyControl figura_fis = new RigidBodyControl(15F);              
            figura_geo.addControl(figura_fis);
            figura_geo.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
            bulletAppState.getPhysicsSpace().add(figura_fis);    
            rootNode.attachChild(figura_geo);  
        }
        
        figura = new Box(0.75F, 1F, 0.75F);    
        float[] coorXF2 = {5F, -5F};
        for (int x = 0; x < coorXF2.length; x++){
            Geometry figura_geo = new Geometry("Figura2", figura);                
            figura_geo.setLocalTranslation(coorXF2[x], 1F, 0F);                           
            figura_geo.setMaterial(prisma_mat);                                                                     
            RigidBodyControl figura_fis = new RigidBodyControl(20F);
            figura_geo.addControl(figura_fis);
            figura_geo.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
            bulletAppState.getPhysicsSpace().add(figura_fis);
            rootNode.attachChild(figura_geo);
        }
    }
    
    /**
     * Método usado para crear la iluminación utilizada durante la partida.
     */
    protected void crearLuz() {
        DirectionalLight sol = new DirectionalLight();                          // Creamos una luz direccional
        sol.setColor(ColorRGBA.White.mult(0.8f));                               // de color blanco sin ser excesivamente brillante,
        sol.setDirection(corLuz.normalizeLocal());                              // le indicamos que proviene de la parte superior derecha de la posición inicial trasera del jugador
        rootNode.addLight(sol);                                                 // y añadimos la luz al grafo
        
        DirectionalLightShadowRenderer sombra =
                new DirectionalLightShadowRenderer(assetManager, 1024, 3);      // Creamos un objeto para renderizar la sombra de la escena
        sombra.setLight(sol);                                                   // a partir de la luz generada por el objeto direccional previamente creado
        viewPort.addProcessor(sombra);                                          // y lo mostramos en el mundo
        
        AmbientLight amb = new AmbientLight();                                  // Creamos una luz ambiental blanca suave para iluminar
        amb.setColor(ColorRGBA.White.mult(0.1f));                               // la escena globalmente brillando por todos los lados
        rootNode.addLight(amb);
    }
    
    /**
     * Método usado para cargar los textos, fuentes y localizaciones 
     * por defecto de estos durante la ejecución del programa.
     */
    protected void cargarTexto(){
        guiNode.detachAllChildren();                                            // Eliminamos todos los componenes que puedan existir en el nodo guiNode
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");         // y creamos la fuente por defecto usada para mostrar los mensajes en el juego
        
        bolasTxt = new BitmapText(guiFont, false);                              // Creamos las variables correspondientes para mostrar los mensajes
        bolasTxt.setSize(guiFont.getCharSet().getRenderedSize());               // asignándoles la fuente a utilizar y renderizándola,
        bolasTxt.setText("Tienes " + numBolas + " disparos");                   // le agregamos un texto y una licalización inicial
        bolasTxt.setLocalTranslation(10, settings.getHeight()-10, 0);
        guiNode.attachChild(bolasTxt);                                          // y la añadimos al nodo guiNode
        
        menuTxt = new BitmapText(guiFont, false);        
        menuTxt.setSize(guiFont.getCharSet().getRenderedSize());                         
        menuTxt.setText("(M) Menú Desactivado\n");
        menuTxt.setLocalTranslation(
                settings.getWidth() - menuTxt.getLineWidth()-30, 
                settings.getHeight() - menuTxt.getLineHeight()/2, 0);        
        guiNode.attachChild(menuTxt);
        
        golesTxt = new BitmapText(guiFont, false);        
        golesTxt.setSize(guiFont.getCharSet().getRenderedSize());                         
        golesTxt.setText("Has marcado " + numGoles + " goles");                             
        golesTxt.setLocalTranslation(
                settings.getWidth() - golesTxt.getLineWidth()-25, 30, 0);        
        guiNode.attachChild(golesTxt);
        
        puntero = new BitmapText(guiFont, false);
        puntero.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        puntero.setText("+");
        puntero.setLocalTranslation(
                settings.getWidth() / 2 - puntero.getLineWidth()/2, 
                settings.getHeight() / 2 + puntero.getLineHeight()/2, 0);
        guiNode.attachChild(puntero);
    }

    /**
     * Método usado para cargar las teclas que utilizaremos durante el juego,
     * a las que asignamos un nombre identificativo para diferenciarlas.
     */
    protected void cargarTeclas() {
        inputManager.addMapping("Lanzar", new KeyTrigger(KeyInput.KEY_SPACE),
                                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(al, new String[]{"Lanzar"});
        inputManager.addMapping("Menu", new KeyTrigger(KeyInput.KEY_M));        // Asignamos una o varias teclas para ccada opción correspondiente
        inputManager.addListener(al, new String[]{"Menu"});                     // agregándolas al vector de las mismas para evaluarlas posteriormente
        inputManager.addMapping("Gravedad", new KeyTrigger(KeyInput.KEY_G));
        inputManager.addListener(al, new String[]{"Gravedad"});
        inputManager.addMapping("Estado", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addListener(al, new String[]{"Estado"});
        inputManager.addMapping("Debug", new KeyTrigger(KeyInput.KEY_B));
        inputManager.addListener(al, new String[]{"Debug"});
    }
  
    /**
     * Método usado para cargar los audios usados durante el juego.
     */
    protected void cargarAudio() {
        click = new AudioNode(assetManager, "Sounds/click.ogg", false);         // Establecemos el archivo origen a reproducir,
        click.setPositional(false);                                             // deshabilitamos el audio posicional para evitar problemas
        rootNode.attachChild(click);                                            // y agregamos el elemento al nodo padre
        
        gol = new AudioNode(assetManager, "Sounds/gol.ogg", false);
        gol.setPositional(false);
        rootNode.attachChild(gol);
        
        cancion = new AudioNode(assetManager, "Sounds/cancion.ogg", false);
        cancion.setPositional(false);
        rootNode.attachChild(cancion);
        cancion.setLooping(true);                                               // Establecemos la reproducción en bucle del elemento
        cancion.play();                                                         // y lo ejecutamos para que suene por defecto automáticamente        
    }
    
    /**
     * Método usado para cargar los elementos necesarios que utilizamos 
     * para generar una explosión a modo de aviso cuando se marca un gol.
     */
    protected void cargarExplosion(){
        efecto = new ParticleEmitter("Efecto", ParticleMesh.Type.Point, 64);    // Creamos el objeto y le asignamos un nombre, tamaño y tipo de partícula
        efecto.setStartColor(ColorRGBA.White);                                  // le asignamos el color inicial y final del efecto 
        efecto.setEndColor(ColorRGBA.Orange);
        efecto.setStartSize(1.5f);                                              // y hacemos lo mismo con el tamaño
        efecto.setEndSize(3f);
        efecto.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
        efecto.setParticlesPerSec(0);
        efecto.setLowLife(.05f);                                                // estableciendo el tiempo mínimo y máximo de duración del efecto
        efecto.setHighLife(.1f);
        efecto.setImagesX(3);
        efecto.setImagesY(3);
        efecto.setMaterial(explosion_mat);                                      // le agregamos el material usado para crearlo y
        efecto.setLocalScale(500f);                                             // lo escalamos de manera que sea visible desde cualquier parte de la escena
        rootNode.attachChild(efecto);
    }
    
    /**
     * Método usado para cambiar el contenido del menú mostrado en el juego,
     * activando o desactivando opciones en función de variables de estado.
     */
    protected void cambiarMenu() {
        if (menu) {                                                             // Si el menú esta activado mostramos las opciones
            menuTxt.setText("(M) Menú Activado\n"                               // correspondientes y su estado (activado o no)
                    + "(G) Gravedad "+((gravedad)?"Activada":"Desactivada")+"\n"
                    + "(E) Estado "+((estado)?"Activado":"Desactivado")+"\n"
                    + "(B) Debug "+((debug)?"Activado":"Desactivado")+"\n");
        } else {
            menuTxt.setText("(M) Menú Desactivado\n");
        }
    }
    
    /**
     * Método usado para crear y lanzar la bola usada durante la partida.
     */
    protected void crearBola() {
        Sphere esfera = new Sphere(16, 16, 0.20f);                              // Creamos una esfera estableciendo su tamaño a partir de su diámetro
        Geometry bola_geo = new Geometry("bola", esfera);                       // Creamos un spatial de tipo geometría para asociarlo a la esfera
        bola_geo.setMaterial(bola_mat);                                         // Asignamos el material usado por la geometría
        bola_geo.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);         // Asignamos proyección y recepción de sombra a la bola
        rootNode.attachChild(bola_geo);                                         // Añadimos la bola al grafo de escena    
        bola_geo.setLocalTranslation(cam.getLocation());                        // Colocamos la bola en la posición de la cámara
        RigidBodyControl bola_fis = new RigidBodyControl(0.5f);                 // Creamos el objeto de control físico asociado a la bola con un peso de medio kilo
        bola_geo.addControl(bola_fis);                                          // Asociamos la geometría de la bola al control físico
        bulletAppState.getPhysicsSpace().add(bola_fis);                         // La añadimos al motor de física
        bola_fis.setLinearVelocity(cam.getDirection().mult(25));                // Empujamos la bola en la dirección que mira la cámara a una velocidad de 25 metros por segundo!    
        bolaN.attachChild(bola_geo);                                            // Agregamos la bola al nodo bolaN para comprobar si entra en la portería
    }
    
    /**
     * Creamos un escuchador de eventos para capturar las opciones
     * utilizadas durante la ejecución de la aplicación.
     */
    protected ActionListener al = new ActionListener() {
        /**
        * Método usado para obtener y gestionar las acciones lanzadas recogidas
        * por el escuchador de eventos durante la ejecución de la aplicación.
        * 
        * @param name String: nombre de la acción que esta siendo ejecutada.
        * @param pulsado boolean: indica si el control esta siendo pulsado o no.
        * @param tpf float: tiempo en segundos que dura el frame actual.
        */
        @Override
        public void onAction(String name, boolean pulsado, float tpf) {
            if (pulsado) {
                corAux = cam.getLocation().clone();                             // Capturamos las coordenadas de la cámara para asegurarnos de que no nos salimos de los límites del campo
                if (name.equals("Lanzar") && numBolas > 0) {                    // Si lanzamos la bola y tenemos algún disparo disponible
                    click.playInstance();                                       // reproducimos un sonido a modo de aviso
                    crearBola();                                                // y creamos la bola correspondiente para ese disparo
                    bolasTxt.setText("Tienes " + --numBolas + " disparos");     // aumentando y mostrando el contador de las mismas utilizado
                } else if (name.equals("Menu")) {                               // Si pulsamos la tecla de menú deshabilitamos o habilitamos el uso del mismo
                    menu = !menu;                                               // y cambiamos mostramos las opciones pertienentes en este
                    cambiarMenu();
                } else if (name.equals("Gravedad") && menu) {                   // Si el menú esta activo y pulsamos la tecla de gravedad
                    if (gravedad) {                                             // habilitamos o no la misma a partir de la física del mundo
                        bulletAppState.getPhysicsSpace().setGravity(
                                Vector3f.ZERO);
                    } else {
                        bulletAppState.getPhysicsSpace().setGravity(
                                new Vector3f(0,-9.8F,0));
                    }
                    gravedad = !gravedad;
                    cambiarMenu();
                } else if (name.equals("Estado") && menu) {                     // Si el menú esta activo y pulsamos la tecla de estado
                    if (estado) {                                               // mostramos o no la opción correspondiente
                        setDisplayStatView(false);
                    } else {
                        setDisplayStatView(true);
                    }
                    estado = !estado;
                    cambiarMenu();
                } else if (name.equals("Debug") && menu) {                      // Si el menú esta activo y pulsamos la tecla de debug
                    if (debug) {                                                // activamos o no la vista de depuración del mundo
                        bulletAppState.setDebugEnabled(false);
                    } else {
                        bulletAppState.setDebugEnabled(true);
                    }
                    debug = !debug;
                    cambiarMenu();
                }
            }
        }
    };

    /**
     * Método sobrecargado utilizado para implementar la lógica del Juego.
     * 
     * @param tpf float: tiempo en segundos que dura el frame actual.
     */
    @Override
    public void simpleUpdate(float tpf) {
        Vector3f camNueva = cam.getLocation().clone();                          // Obtenemos un vector con las coordenadas actuales de la cámara
        if (camNueva.x > -anchoM+1F && camNueva.x < anchoM-1F && 
            camNueva.y > 1.1F && camNueva.y < 5.0F &&                           // Si las coordenadas de la cámara superan los límites del eje z, y o z del campo
            camNueva.z > -largoM+1.5F && camNueva.z < largoM-1.5F) {            // reasignamos las mismas a la posición previa al movimiento realizado
            corAux = camNueva;
        }                                                                       //tamaño largoM-1
        cam.setLocation(corAux);
        
        if (defensaN.getChild(1).getWorldTranslation().x > -10 && swD) {        // Movemos los elementos del nodo defensaN y porteroN sobre su eje x
            defensaN.move(-4F * tpf, 0, 0);                                     // mientras no supere los límites establecidos estableciendo un bucle
            porteroN.move(2F * tpf, 0, 0);                                      // infinito de derecha a izquierdo e izquierda a derecha respectivamente
            if (defensaN.getChild(1).getWorldTranslation().x < -10) {
                swD = !swD;
            }
        } else {
            defensaN.move(4F * tpf, 0, 0);
            porteroN.move(-2F * tpf, 0, 0);
            if (defensaN.getChild(0).getWorldTranslation().x > 10) {
                swD = !swD;
            }
        } 
        
        if (bolaN.getQuantity() > 0) {                                          // Si la cantidad de bolas contenidas en el nodo bolaN es superior a 0
            for (int i = 0; i < bolaN.getQuantity(); i++) {                     // recorremos los elementos de dicho nodo para comprobar 
                corBola = bolaN.getChild(i).getWorldTranslation();              // si han sobrepasado los límites establecidos para la portería
                if (corBola.x > -anchoP/2+0.3F && corBola.x < anchoP/2-0.3F && 
                    corBola.y > 0F && corBola.y < altoP-0.3F && 
                    corBola.z > -largoM+3F && corBola.z < -largoM+5.3F) {
                    bolaN.detachChildAt(i);                                     // Si la bola entra en la portería la eliminamos del nodo bolaN,
                    gol.playInstance();                                         // reproducimos un sonido a modo de aviso,
                    golesTxt.setText("Has marcado " + ++numGoles + " goles");   // incrementamos y mostramos el número de goles marcados
                    efecto.emitAllParticles();                                  // y lanzamos un efecto a modo de aviso
                }
            }
        }
    }
}