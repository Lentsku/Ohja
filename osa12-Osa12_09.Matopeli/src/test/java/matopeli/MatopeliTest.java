package matopeli;

import matopeli.domain.Suunta;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MatopeliTest<_P, _O, _M, _G, _N, _A, _K> {

    private String pakkaus = "matopeli.domain";
    private String palaLuokka = "Pala";
    private String omenaLuokka = "Omena";
    private String matoLuokka = "Mato";
    private String matopeliLuokka = "Matopeli";

    private Class palaClass;
    private Class omenaClass;
    private Class matoClass;
    private Class matopeliClass;

    Reflex.ClassRef<_P> _PRef;
    Reflex.ClassRef<_O> _ORef;
    Reflex.ClassRef<_M> _MRef;
    Reflex.ClassRef<_G> _GRef;
    Reflex.ClassRef<_N> _NRef;
    Reflex.ClassRef<_A> _ARef;

    @Before
    public void setUp() {
        try {
            String pala = pakkaus + "." + palaLuokka;
            palaClass = ReflectionUtils.findClass(pala);
            _PRef = Reflex.reflect(pala);

            String omena = pakkaus + "." + omenaLuokka;
            omenaClass = ReflectionUtils.findClass(omena);
            _ORef = Reflex.reflect(omena);

            String mato = pakkaus + "." + matoLuokka;
            matoClass = ReflectionUtils.findClass(mato);
            _MRef = Reflex.reflect(mato);

            String game = pakkaus + "." + matopeliLuokka;
            matopeliClass = ReflectionUtils.findClass(game);
            _GRef = Reflex.reflect(game);

        } catch (Throwable e) {
        }
    }

    @Test
    @Points(Tehtava.ID + ".1")
    public void onPala() throws Throwable {
        assertNotNull("Loithan luokan " + palaLuokka + " pakkaukseen " + pakkaus + "?", palaClass);
        assertTrue("Tee luokalle Pala konstruktori public Pala(int x, int y)", _PRef.constructor().taking(int.class, int.class).isPublic());
        assertTrue("Tee luokalle Pala metodi public int getX()", _PRef.method("getX").returning(int.class).takingNoParams().isPublic());
        assertTrue("Tee luokalle Pala metodi public int getY()", _PRef.method("getY").returning(int.class).takingNoParams().isPublic());

        String v = "Pala p1 = new Pala(2,4);\n";

        _P kakkosNelonen = _PRef.constructor().taking(int.class, int.class).withNiceError(v).invoke(2, 4);

        v += "p1.getX()\n";
        int x = _PRef.method("getX").returning(int.class).takingNoParams().withNiceError(v).invokeOn(kakkosNelonen);
        assertEquals(v, 2, x);

        v += "p1.getY()\n";
        int y = _PRef.method("getY").returning(int.class).takingNoParams().withNiceError(v).invokeOn(kakkosNelonen);
        assertEquals(v, 4, y);

        _P kolmosKasi = _PRef.constructor().taking(int.class, int.class).invoke(3, 8);
        String vv = "Pala p4 = new Pala(3,8);\n"
                + "p4.getX()\n";
        x = _PRef.method("getX").returning(int.class).takingNoParams().withNiceError(v).invokeOn(kolmosKasi);

        assertEquals(vv, 3, x);

        vv = "Pala p4 = new Pala(3,8);\n"
                + "p4.getX()\n";
        x = _PRef.method("getY").returning(int.class).takingNoParams().withNiceError(v).invokeOn(kolmosKasi);

        assertEquals(vv, 8, x);

        v = ""
                + "Pala p1 = new Pala(2,4);\n"
                + "Pala p2 = new Pala(2,4);\n"
                + "p1.osuu(p2);\n";
        _P toinenKakkosNelonen = newPala(2, 4);

        assertTrue("Tee luokalle Pala metodi public boolean osuu(Pala toinen)", _PRef.method("osuu").returning(boolean.class).taking(palaClass).isPublic());

        boolean samat = _PRef.method(kakkosNelonen, "osuu").returning(boolean.class).taking(_PRef.cls()).invoke(toinenKakkosNelonen);
        assertEquals(v, true, samat);

        v = ""
                + "Pala p1 = new Pala(2,4);\n"
                + "Pala p3 = new Pala(3,4);\n"
                + "p1.osuu(p3);\n";

        _P kolmosNelonen = _PRef.constructor().taking(int.class, int.class).invoke(3, 4);
        samat = _PRef.method(kakkosNelonen, "osuu").returning(boolean.class).taking(_PRef.cls()).invoke(kolmosNelonen);
        assertEquals(v, false, samat);

        v = ""
                + "Pala p1 = new Pala(2,4);\n"
                + "Pala p3 = new Pala(4,3);\n"
                + "p1.osuu(p3);\n";

        _P nelosKolmonen = _PRef.constructor().taking(int.class, int.class).invoke(4, 3);
        samat = _PRef.method("osuu").returning(boolean.class).taking(_PRef.cls()).invokeOn(nelosKolmonen, toinenKakkosNelonen);
        assertEquals(v, false, samat);

        v = ""
                + "Pala p1 = new Pala(2,4);\n"
                + "Pala p3 = new Pala(2,3);\n"
                + "p1.osuu(p3);\n";

        _P kakkosKolmonen = _PRef.constructor().taking(int.class, int.class).invoke(2, 3);
        samat = _PRef.method("osuu").returning(boolean.class).taking(_PRef.cls()).invokeOn(kakkosKolmonen, toinenKakkosNelonen);
        assertEquals(v, false, samat);

        String tulostus = nelosKolmonen.toString();

        assertFalse("Tee luokalle Pala tehtävänannon mukaisesti toimiva toString", tulostus.contains("@"));
        assertEquals("Pala p = new Pala(4,3);\n"
                + "System.out.println(p);\n", "(4,3)", tulostus.replaceAll("\\s+", ""));
    }

    @Test
    @Points(Tehtava.ID + ".1")
    public void onOmena() {
        assertNotNull("Loithan luokan " + omenaLuokka + " pakkaukseen " + pakkaus + "?", omenaClass);
        assertTrue("Periihän luokka " + omenaLuokka + " luokan " + palaLuokka + "?", palaClass.isAssignableFrom(omenaClass));
        assertTrue("Tee luokalle Omena konstruktori public Omena(int    x, int y)", Reflex.reflect(omenaClass).constructor().taking(int.class, int.class).isPublic());
        assertTrue("Luokalla Omena pitäisi olla perittynä metodi public int getX()", Reflex.reflect(omenaClass).method("getX").returning(int.class).takingNoParams().isPublic());
        assertTrue("Luokalla Omena pitäisi olla perittynä metodi public int getY()", Reflex.reflect(omenaClass).method("getY").returning(int.class).takingNoParams().isPublic());
        assertTrue("Luokalla Omena pitäisi olla perittynä metodi public boolean osuu(Pala toinen)", Reflex.reflect(omenaClass).method("osuu").returning(boolean.class).taking(palaClass).isPublic());

        assertTrue("Onhan luokalla " + omenaLuokka + " 0 oliomuuttujaa?", omenaClass.getDeclaredFields().length == 0);
        assertTrue("Onhan luokalla " + omenaLuokka + " 0 omaa metodia?", omenaClass.getDeclaredMethods().length == 0);
    }

    /*
     *
     */
    @Test
    @Points(Tehtava.ID + ".2")
    public void onMato() throws Throwable {
        assertNotNull("Loithan luokan " + matoLuokka + " pakkaukseen " + pakkaus + "?", matoClass);

        assertTrue("Tee luokalle Mato konstruktori public Mato(int alkuX, int alkuY, Suunta alkusuunta)", _MRef.constructor().taking(int.class, int.class, Suunta.class).isPublic());

        saniteettitarkastus(pakkaus + "." + matoLuokka, 10, "");

        assertTrue("Tee luokalle Mato metodi public Suunta getSuunta()",
                _MRef.method("getSuunta").returning(Suunta.class).takingNoParams().isPublic());

        String v = "Mato m = new Mato(1, 1, Suunta.OIKEA);\n";
        _M mato = _MRef.constructor().taking(int.class, int.class, Suunta.class).withNiceError(v).invoke(1, 1, Suunta.OIKEA);
        v = "Mato m = new Mato(1, 1, Suunta.OIKEA);\n"
                + "m.getSuunta();\n";
        assertEquals(Suunta.OIKEA, _MRef.method(mato, "getSuunta").returning(Suunta.class).takingNoParams().withNiceError(v).invoke());

        assertTrue("Tee luokalle Mato metodi public void setSuunta(Suunta suunta)", _MRef.method("setSuunta").returningVoid().taking(Suunta.class).isPublic());

        v = "Mato m = new Mato(1, 1, Suunta.OIKEA);\n"
                + "m.setSuunta(Suunta.ALAS);\n"
                + "m.getSuunta();\n";
        kaanny(mato, Suunta.ALAS, v);
        assertEquals(v, Suunta.ALAS, _MRef.method(mato, "getSuunta").returning(Suunta.class).takingNoParams().withNiceError(v).invoke());

        v = "Mato m = new Mato(1, 1, Suunta.OIKEA);\n"
                + "m.setSuunta(Suunta.ALAS);\n"
                + "m.setSuunta(Suunta.YLOS);\n"
                + "m.getSuunta();\n";
        _MRef.method(mato, "setSuunta").returningVoid().taking(Suunta.class).withNiceError(v).invoke(Suunta.YLOS);
        assertEquals(v, Suunta.YLOS, _MRef.method(mato, "getSuunta").returning(Suunta.class).takingNoParams().withNiceError(v).invoke());

        mato = _MRef.constructor().taking(int.class, int.class, Suunta.class).withNiceError(v).invoke(1, 1, Suunta.OIKEA);

        v = "Mato m = new Mato(1, 1, Suunta.OIKEA);\n"
                + "m.getPituus();\n";
        assertTrue("Tee luokalle Mato metodi public int getPituus()", _MRef.method("getPituus").returning(int.class).takingNoParams().isPublic());
        assertEquals(v, 1, (int) pituus(mato, v));

        assertTrue("Tee luokalle Mato metodi public List<Pala> getPalat()", _MRef.method("getPalat").returning(List.class).takingNoParams().isPublic());

        v = "Mato m = new Mato(1, 1, Suunta.OIKEA);\n"
                + "m.getPalat();\n";

        _P p = newPala(1, 1);
        List<_P> exp = new ArrayList<_P>();
        exp.add(p);

        List<_P> ret = _MRef.method(mato, "getPalat").returning(List.class).takingNoParams().invoke();
        assertTrue(v + "palautit: " + exp, samat(exp, ret));

        mato = _MRef.constructor().taking(int.class, int.class, Suunta.class).withNiceError(v).invoke(3, 5, Suunta.OIKEA);
        v = "Mato m = new Mato(3, 5, Suunta.OIKEA);\n"
                + "m.getPalat();\n";

        p = newPala(3, 5);
        exp = new ArrayList<_P>();
        exp.add(p);

        ret = palat(mato, v);
        assertTrue(v + "palautit: " + ret, samat(exp, ret));

        // metodi liiku
        assertTrue("Tee luokalle Mato metodi public void liiku()", _MRef.method("liiku").returningVoid().takingNoParams().isPublic());

        mato = newMato(1, 1, Suunta.OIKEA, v);
        v = "Mato m = new Mato(1, 1, Suunta.OIKEA);\n"
                + "m.liiku();\n"
                + "m.getPalat();\n";

        liiku(mato, v);

        exp = new ArrayList<_P>();
        exp.add(newPala(1, 1));
        exp.add(newPala(2, 1));
        ret = palat(mato, v);
        assertTrue(v + "palautit: " + ret, samat(exp, ret));

        v = "Mato m = new Mato(1, 1, Suunta.OIKEA);\n"
                + "m.liiku();\n"
                + "m.getPituus();\n";

        assertEquals(v, 2, (int) pituus(mato, v));

        v = "Mato m = new Mato(1, 1, Suunta.OIKEA);\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.getPalat();\n";

        liiku(mato, v);
        liiku(mato, v);

        exp = new ArrayList<_P>();
        exp.add(newPala(2, 1));
        exp.add(newPala(3, 1));
        exp.add(newPala(4, 1));
        ret = palat(mato, v);
        assertTrue(v + "palautit: " + ret, samat(exp, ret));

        v = "Mato m = new Mato(1, 1, Suunta.OIKEA);\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.getPituus();\n";

        assertEquals(v, 3, (int) pituus(mato, v));

        v = "Mato m = new Mato(1, 1, Suunta.OIKEA);\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.pituus();\n";

        assertEquals(v, 3, (int) pituus(mato, v));

        // liiku vasemmalle
        mato = newMato(5, 1, Suunta.VASEN, v);
        v = "Mato m = new Mato(1, 1, Suunta.VASEN);\n"
                + "m.liiku();\n"
                + "m.getPalat();\n";

        liiku(mato, v);

        exp = new ArrayList<_P>();
        exp.add(newPala(5, 1));
        exp.add(newPala(4, 1));
        ret = palat(mato, v);
        assertTrue(v + "palautit: " + ret, samat(exp, ret));

        v = "Mato m = new Mato(5, 1, Suunta.VASEN);\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.getPalat();\n";

        liiku(mato, v);
        liiku(mato, v);

        exp = new ArrayList<_P>();
        exp.add(newPala(4, 1));
        exp.add(newPala(3, 1));
        exp.add(newPala(2, 1));
        ret = palat(mato, v);
        assertTrue(v + "palautit: " + ret, samat(exp, ret));

        // liiku alas
        mato = newMato(5, 1, Suunta.ALAS, v);
        v = "Mato m = new Mato(5, 1, Suunta.ALAS);\n"
                + "m.liiku();\n"
                + "m.getPalat();\n";

        liiku(mato, v);

        exp = new ArrayList<_P>();
        exp.add(newPala(5, 1));
        exp.add(newPala(5, 2));
        ret = palat(mato, v);
        assertTrue(v + "palautit: " + ret, samat(exp, ret));

        v = "Mato m = new Mato(5, 1, Suunta.ALAS);\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.getPalat();\n";

        liiku(mato, v);
        liiku(mato, v);

        exp = new ArrayList<_P>();
        exp.add(newPala(5, 2));
        exp.add(newPala(5, 3));
        exp.add(newPala(5, 4));
        ret = palat(mato, v);
        assertTrue(v + "palautit: " + ret, samat(exp, ret));

        // liiku ylös
        mato = newMato(5, 5, Suunta.YLOS, v);
        v = "Mato m = new Mato(5, 5, Suunta.YLOS);\n"
                + "m.liiku();\n"
                + "m.getPalat();\n";

        liiku(mato, v);

        exp = new ArrayList<_P>();
        exp.add(newPala(5, 5));
        exp.add(newPala(5, 4));
        ret = palat(mato, v);
        assertTrue(v + "palautit: " + ret, samat(exp, ret));

        v = "Mato m = new Mato(5, 5, Suunta.YLOS);\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.getPalat();\n";

        liiku(mato, v);
        liiku(mato, v);

        exp = new ArrayList<_P>();
        exp.add(newPala(5, 4));
        exp.add(newPala(5, 3));
        exp.add(newPala(5, 2));
        ret = palat(mato, v);
        assertTrue(v + "palautit: " + ret, samat(exp, ret));

        // käänny
        v = "Mato m = new Mato(5, 5, Suunta.YLOS);\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.setSuunta(Suunta.OIKEA)\n"
                + "m.liiku();"
                + "m.liiku();"
                + "m.getPalat();\n";

        kaanny(mato, Suunta.OIKEA, v);
        liiku(mato, v);
        liiku(mato, v);

        exp = new ArrayList<_P>();
        exp.add(newPala(5, 2));
        exp.add(newPala(6, 2));
        exp.add(newPala(7, 2));
        ret = palat(mato, v);
        assertTrue(v + "palautit: " + ret, samat(exp, ret));

        v = "Mato m = new Mato(5, 5, Suunta.YLOS);\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.setSuunta(Suunta.OIKEA)\n"
                + "m.liiku();"
                + "m.liiku();"
                + "m.setSuunta(Suunta.ALAS);"
                + "m.liiku();"
                + "m.getPalat();\n";

        kaanny(mato, Suunta.ALAS, v);
        liiku(mato, v);

        exp = new ArrayList<_P>();
        exp.add(newPala(6, 2));
        exp.add(newPala(7, 2));
        exp.add(newPala(7, 3));
        ret = palat(mato, v);
        assertTrue(v + "palautit: " + ret, samat(exp, ret));
        v = "Mato m = new Mato(5, 5, Suunta.YLOS);\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.setSuunta(Suunta.OIKEA)\n"
                + "m.liiku();"
                + "m.liiku();"
                + "m.setSuunta(Suunta.ALAS);"
                + "m.liiku();"
                + "m.getPalat();\n"
                + "m.setSuunta(Suunta.ALAS);"
                + "m.liiku();"
                + "m.liiku();"
                + "m.liiku();"
                + "";

        kaanny(mato, Suunta.VASEN, v);
        liiku(mato, v);
        liiku(mato, v);
        liiku(mato, v);

        exp = new ArrayList<_P>();
        exp.add(newPala(6, 3));
        exp.add(newPala(5, 3));
        exp.add(newPala(4, 3));
        ret = palat(mato, v);
        assertTrue(v + "palautit: " + ret, samat(exp, ret));

        // kasva
        assertTrue("Tee luokalle Mato metodi public void kasva()", _MRef.method("kasva").returningVoid().takingNoParams().isPublic());

        mato = newMato(5, 5, Suunta.YLOS, v);
        v = "Mato m = new Mato(5, 5, Suunta.YLOS);\n"
                + "m.kasva();\n";

        kasva(mato, v);

        v = "Mato m = new Mato(5, 5, Suunta.YLOS);\n"
                + "m.kasva();\n"
                + "m.getPalat();\n";

        exp = new ArrayList<_P>();
        exp.add(newPala(5, 5));
        ret = palat(mato, v);
        assertTrue("Huomaa, että mato kasvaa vasta liikkuessa!\n" + v + "palautit: " + ret, samat(exp, ret));

        v = "Mato m = new Mato(5, 5, Suunta.YLOS);\n"
                + "m.kasva();\n"
                + "m.getPituus();\n";

        assertEquals("Huomaa, että mato kasvaa vasta liikkuessa!\n" + v, 1, (int) pituus(mato, v));

        // kasva ja liiku
        mato = newMato(5, 5, Suunta.YLOS, v);
        v = "Mato m = new Mato(5, 5, Suunta.YLOS);\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.kasva();\n"
                + "m.liiku();\n"
                + "m.getPalat()\n";

        liiku(mato, v);
        liiku(mato, v);
        kasva(mato, v);
        liiku(mato, v);

        exp = new ArrayList<_P>();
        exp.add(newPala(5, 5));
        exp.add(newPala(5, 4));
        exp.add(newPala(5, 3));
        exp.add(newPala(5, 2));
        ret = palat(mato, v);

        assertTrue("Madon pitäisi kasvaa\n" + v + "palautit: " + ret, samat(exp, ret));

        v = "Mato m = new Mato(5, 5, Suunta.YLOS);\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.kasva();\n"
                + "m.liiku();\n"
                + "m.getPituus();\n";

        assertEquals("Madon pitäisi kasvaa\n" + v, 4, (int) pituus(mato, v));

        // ei kasva liikaa
        v = "Mato m = new Mato(5, 5, Suunta.YLOS);\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.kasva();\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.getPalat()\n";

        liiku(mato, v);

        exp = new ArrayList<_P>();
        exp.add(newPala(5, 4));
        exp.add(newPala(5, 3));
        exp.add(newPala(5, 2));
        exp.add(newPala(5, 1));
        ret = palat(mato, v);

        assertTrue("Madon pitäisi kasvaa vain kerran per metodikutsu\n" + v + "palautit: " + ret, samat(exp, ret));

        assertEquals("Madon pitäisi kasvaa vain kerran per metodikutsu\n" + v, 4, (int) pituus(mato, v));

        v = "Mato m = new Mato(5, 5, Suunta.YLOS);\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.kasva();\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.setSuunta(Suunta.OIKEA);\n"
                + "m.kasva();\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.getPalat()\n";

        kaanny(mato, Suunta.OIKEA, v);
        kasva(mato, v);
        liiku(mato, v);
        liiku(mato, v);

        exp = new ArrayList<_P>();
        exp.add(newPala(5, 3));
        exp.add(newPala(5, 2));
        exp.add(newPala(5, 1));
        exp.add(newPala(6, 1));
        exp.add(newPala(7, 1));
        ret = palat(mato, v);

        assertTrue(v + "palautit: " + ret, samat(exp, ret));

        v = "Mato m = new Mato(5, 5, Suunta.YLOS);\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.kasva();\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.setSuunta(Suunta.OIKEA);\n"
                + "m.kasva();\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.setSuunta(Suunta.ALAS);\n"
                + "m.kasva();\n"
                + "m.liiku();\n"
                + "m.kasva();\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.getPalat()\n";

        kaanny(mato, Suunta.ALAS, v);
        kasva(mato, v);
        liiku(mato, v);
        kasva(mato, v);
        liiku(mato, v);
        liiku(mato, v);

        exp = new ArrayList<_P>();
        exp.add(newPala(5, 2));
        exp.add(newPala(5, 1));
        exp.add(newPala(6, 1));
        exp.add(newPala(7, 1));
        exp.add(newPala(7, 2));
        exp.add(newPala(7, 3));
        exp.add(newPala(7, 4));
        ret = palat(mato, v);

        assertTrue(v + "palautit: " + ret, samat(exp, ret));

        // liian aikainen kasvu ei tee mitään
        mato = newMato(5, 5, Suunta.YLOS, v);
        v = "Mato m = new Mato(5, 5, Suunta.YLOS);\n"
                + "m.liiku();\n"
                + "m.kasva();\n"
                + "m.liiku();\n"
                + "m.liiku();\n"
                + "m.getPalat()\n";

        liiku(mato, v);
        kasva(mato, v);
        liiku(mato, v);
        liiku(mato, v);

        exp = new ArrayList<_P>();
        exp.add(newPala(5, 4));
        exp.add(newPala(5, 3));
        exp.add(newPala(5, 2));
        ret = palat(mato, v);

        assertTrue("Jos madon pituus on 1 tai 2 ja metodia kasva kutsutaan, ei sillä saa olla vaikutusta"
                + "\n" + v + "palautit: " + ret, samat(exp, ret));

        // osuu
        assertTrue("Tee luokalle Mato metodi public boolean osuu(Pala pala)", _MRef.method("osuu").returning(boolean.class).taking(_PRef.cls()).isPublic());

        _M mato2 = newMato(5, 5, Suunta.YLOS, v);
        p = newPala(5, 5);

        v = ""
                + "Mato m = new Mato(5, 5, Suunta.YLOS, v);\n"
                + "m.osuu(new Pala(5,5));\n";
        assertEquals(v, true, _MRef.method(mato2, "osuu").returning(boolean.class).taking(_PRef.cls()).withNiceError(v).invoke(p));

        mato2 = newMato(4, 6, Suunta.YLOS, v);
        p = newPala(4, 6);

        v = ""
                + "Mato m = new Mato(5, 5, Suunta.YLOS, v);\n"
                + "m.osuu(new Pala(5,5));\n";
        assertEquals(v, true, _MRef.method(mato2, "osuu").returning(boolean.class).taking(_PRef.cls()).withNiceError(v).invoke(p));

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == 4 && j == 6) {
                    continue;
                }
                p = newPala(i, j);
                v = ""
                        + "Mato m = new Mato(4, 6, Suunta.YLOS, v);\n"
                        + "m.osuu(new Pala(" + i + "," + j + "));\n";
                assertEquals(v, false, _MRef.method(mato2, "osuu").returning(boolean.class).taking(_PRef.cls()).withNiceError(v).invoke(p));

            }
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == 4 && j == 6) {
                    continue;
                }
                p = newPala(i, j);

                v = "Mato m = new Mato(5, 5, Suunta.YLOS);\n"
                        + "m.liiku();\n"
                        + "m.liiku();\n"
                        + "m.kasva();\n"
                        + "m.liiku();\n"
                        + "m.liiku();\n"
                        + "m.setSuunta(Suunta.OIKEA);\n"
                        + "m.kasva();\n"
                        + "m.liiku();\n"
                        + "m.liiku();\n"
                        + "m.setSuunta(Suunta.ALAS);\n"
                        + "m.kasva();\n"
                        + "m.liiku();\n"
                        + "m.kasva();\n"
                        + "m.liiku();\n"
                        + "m.liiku();\n"
                        + "m.getPalat()\n"
                        + "m.osuu(new Pala(" + i + "," + j + "));\nmadon palat: " + exp + "\n";
                boolean vast = sis(exp, p);
                assertEquals(v, vast, _MRef.method(mato, "osuu").returning(boolean.class).taking(_PRef.cls()).withNiceError(v).invoke(p));
            }

        }

        // osuu itseensa
        assertTrue("Tee luokalle Mato metodi public boolean osuuItseensa()",
                _MRef.method("osuuItseensa").returning(boolean.class).takingNoParams().isPublic());

        mato = newMato(3, 3, Suunta.OIKEA, v);
        v = ""
                + "Mato m = new Mato(3, 3, Suunta.OIKEA);\n"
                + "m.osuuItseensa();\n";

        assertEquals(v, false, osuuItseensa(mato, v));
        liiku(mato, v);

        v = ""
                + "Mato m = new Mato(3, 3, Suunta.OIKEA);\n"
                + "m.liiku();\n"
                + "m.osuuItseensa();\n";

        assertEquals(v, false, osuuItseensa(mato, v));

        kaanny(mato, Suunta.VASEN, v);
        liiku(mato, v);
        v = ""
                + "Mato m = new Mato(3, 3, Suunta.OIKEA);\n"
                + "m.liiku();\n"
                + "m.setSuunta(Suunta.VASEN);\n"
                + "m.liiku();\n"
                + "m.osuuItseensa();\n";

        assertEquals(v, true, osuuItseensa(mato, v));

        mato = newMato(3, 3, Suunta.OIKEA, v);
        v = ""
                + "Mato m = new Mato(3, 3, Suunta.OIKEA);\n"
                + "m.osuuItseensa();\n";

        assertEquals(v, false, osuuItseensa(mato, v));

        liiku(mato, v);

        v = ""
                + "Mato m = new Mato(3, 3, Suunta.OIKEA);\n"
                + "m.liiku();\n"
                + "m.osuuItseensa();\n";

        assertEquals(v, false, osuuItseensa(mato, v));

        kaanny(mato, Suunta.ALAS, v);
        kasva(mato, v);
        liiku(mato, v);
        v = ""
                + "Mato m = new Mato(3, 3, Suunta.OIKEA);\n"
                + "m.liiku();\n"
                + "m.setSuunta(Suunta.ALAS);\n"
                + "m.kasva();\n"
                + "m.liiku();\n"
                + "m.osuuItseensa();\n";

        assertEquals(v, false, osuuItseensa(mato, v));

        kaanny(mato, Suunta.VASEN, v);
        kasva(mato, v);
        liiku(mato, v);

        v = ""
                + "Mato m = new Mato(3, 3, Suunta.OIKEA);\n"
                + "m.liiku();\n"
                + "m.setSuunta(Suunta.ALAS);\n"
                + "m.kasva();\n"
                + "m.liiku();\n"
                + "m.setSuunta(Suunta.VASEN);\n"
                + "m.kasva();\n"
                + "m.liiku();\n"
                + "m.osuuItseensa();\n";

        assertEquals(v, false, osuuItseensa(mato, v));

        v = ""
                + "Mato m = new Mato(3, 3, Suunta.OIKEA);\n"
                + "m.liiku();\n"
                + "m.setSuunta(Suunta.ALAS);\n"
                + "m.kasva();\n"
                + "m.liiku();\n"
                + "m.setSuunta(Suunta.VASEN);\n"
                + "m.kasva();\n"
                + "m.liiku();\n"
                + "m.osuuItseensa();\n"
                + "m.setSuunta(Suunta.YLOS);\n"
                + "m.kasva();\n"
                + "m.liiku();\n"
                + "m.osuuItseensa();\n";

        kaanny(mato, Suunta.YLOS, v);
        kasva(mato, v);
        liiku(mato, v);

        assertEquals(v, true, osuuItseensa(mato, v));
    }

    @Test
    @Points(Tehtava.ID + ".2")
    public void matoTekeeYmpyran() throws Throwable {
        _M mato = newMato(1, 1, Suunta.OIKEA, "");
        liikuYmpyra(mato, 1, 1);
    }

    @Test
    @Points(Tehtava.ID + ".3")
    public void onLuokkaMatopeliJaSenMetodit() throws Throwable {

        assertNotNull("Onhan olemassa luokka " + matopeliLuokka + " pakkauksessa " + pakkaus + "?", matopeliClass);

        saniteettitarkastus(pakkaus + "." + matopeliLuokka, 10, "");

        assertTrue("Tee luokalle Matopeli metodi public Mato getMato()", _GRef.method("getMato").returning(matoClass).takingNoParams().isPublic());

        String v = ""
                + "Matopeli mp = Matopeli(10,10)\n"
                + "Mato m = mp.getMato();\n";
        Object mp = Reflex.reflect(matopeliClass).constructor().taking(int.class, int.class).invoke(10, 10);

        _M mato = _GRef.method((_G) mp, "getMato").returning(_MRef.cls()).takingNoParams().withNiceError(v).invoke();

        assertFalse("Palautettu mato ei saa olla null koodilla\n" + v, mato == null);

        assertEquals(v + "m.getPituus();\n", 1, (int) pituus(mato, v + "m.getPituus();"));

        assertEquals(v + "m.getSuunta();\n", Suunta.ALAS,
                _MRef.method(mato, "getSuunta")
                        .returning(Suunta.class).takingNoParams().withNiceError(v + "m.getSuunta();\n").invoke());

        List<_P> ret = palat(mato, v + "m.getPalat();");

        List<_P> exp = new ArrayList<_P>();
        exp.add(newPala(5, 5));
        ret = palat(mato, v);
        assertTrue("Madon pitäisi olla alussa keskellä\n" + v + "m.getPalat();\n" + "palautit: " + ret, samat(exp, ret));

        assertTrue("Tee luokalle Matopeli metodi public void setMato(Mato mato)", _GRef.method("setMato").returningVoid().taking(_MRef.cls()).isPublic());

        v = ""
                + "Matopeli mp = Matopeli(10,10)\n"
                + "Mato m = new Mato(1,1,Suunta.YLOS);\n"
                + "mp.setMato(m);\n";

        _M mato2 = newMato(1, 1, Suunta.YLOS, "");
        _GRef.method((_G) mp, "setMato").returningVoid().taking(_MRef.cls()).withNiceError(v).invoke(mato2);

        v = ""
                + "Matopeli mp = Matopeli(10,10)\n"
                + "Mato m = new Mato(1,1,Suunta.YLOS);\n"
                + "mp.setMato(m);\n"
                + "m == mp.getMato()";

        _M mato3 = _GRef.method((_G) mp, "getMato").returning(_MRef.cls()).takingNoParams().withNiceError(v).invoke();

        assertEquals("Asettuuko uusi mato? Tarkasta koodi:\n" + v, true, mato3 == mato2);

    }

    @Test
    @Points(Tehtava.ID + ".3")
    public void matoAloittaaAinaKeskelta() throws Throwable {

        int leveys = 20;
        int korkeus = 10;

        for (int i = 0; i < 10; i++) {
            Object peli = Reflex.reflect(matopeliClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);
            Object mato = Reflex.reflect(matopeliClass).method("getMato").returning(matoClass).takingNoParams().invokeOn(peli);

            if (mato == null) {
                fail("Onhan luokan " + matopeliLuokka + " metodilla getMato() määre public, ja luothan uuden madon konstruktorissa.");
            }

            List palat = (List) Reflex.reflect(matoClass).method("getPalat").returning(List.class).takingNoParams().invokeOn(mato);

            if (palat == null || palat.size() != 1 || !palat.get(0).getClass().equals(palaClass)) {
                fail("Juuri luodun madon pituuden tulee olla 1. Tällöin madon metodin getPalat tulee palauttaa lista, jossa on 1 Pala-tyyppistä oliota.");
            }

            int x = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(palat.get(0));
            assertTrue("Juuri luodun madon ensimmäisen palan tulee olla keskellä ruutua. Jos leveys on " + leveys + ", tulee ensimmäisen palan x-koordinaatin olla " + (leveys / 2), x == (leveys / 2));
            int y = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(palat.get(0));
            assertTrue("Juuri luodun madon ensimmäisen palan tulee olla keskellä ruutua. Jos korkeus on " + korkeus + ", tulee ensimmäisen palan y-koordinaatin olla " + (korkeus / 2), y == (korkeus / 2));
        }
    }

    @Test
    @Points(Tehtava.ID + ".4")
    public void omenanLuominenMatopeliin() throws Throwable {

        assertNotNull("Onhan olemassa luokka " + matopeliLuokka + " pakkauksessa " + pakkaus + "?", matopeliClass);

        saniteettitarkastus(pakkaus + "." + matopeliLuokka, 10, "");

        assertTrue("Tee luokalle Matopeli metodi public Mato getMato()", _GRef.method("getMato").returning(matoClass).takingNoParams().isPublic());

        String v = ""
                + "Matopeli mp = Matopeli(10,10)\n"
                + "Mato m = mp.getMato();\n";
        Object mp = Reflex.reflect(matopeliClass).constructor().taking(int.class, int.class).invoke(10, 10);

        _M mato = _GRef.method((_G) mp, "getMato").returning(_MRef.cls()).takingNoParams().withNiceError(v).invoke();

        assertFalse("Palautettu mato ei saa olla null koodilla\n" + v, mato == null);

        assertEquals(v + "m.getPituus();\n", 1, (int) pituus(mato, v + "m.getPituus();"));

        assertEquals(v + "m.getSuunta();\n", Suunta.ALAS,
                _MRef.method(mato, "getSuunta")
                        .returning(Suunta.class).takingNoParams().withNiceError(v + "m.getSuunta();\n").invoke());

        List<_P> ret = palat(mato, v + "m.getPalat();");

        List<_P> exp = new ArrayList<_P>();
        exp.add(newPala(5, 5));
        ret = palat(mato, v);
        assertTrue("Madon pitäisi olla alussa keskellä\n" + v + "m.getPalat();\n" + "palautit: " + ret, samat(exp, ret));

        assertTrue("Tee luokalle Matopeli metodi public void setMato(Mato mato)", _GRef.method("setMato").returningVoid().taking(_MRef.cls()).isPublic());

        v = ""
                + "Matopeli mp = Matopeli(10,10)\n"
                + "Mato m = new Mato(1,1,Suunta.YLOS);\n"
                + "mp.setMato(m);\n";

        _M mato2 = newMato(1, 1, Suunta.YLOS, "");
        _GRef.method((_G) mp, "setMato").returningVoid().taking(_MRef.cls()).withNiceError(v).invoke(mato2);

        v = ""
                + "Matopeli mp = Matopeli(10,10)\n"
                + "Mato m = new Mato(1,1,Suunta.YLOS);\n"
                + "mp.setMato(m);\n"
                + "m == mp.getMato()";

        _M mato3 = _GRef.method((_G) mp, "getMato").returning(_MRef.cls()).takingNoParams().withNiceError(v).invoke();

        assertEquals("Asettuuko uusi mato? Tarkasta koodi:\n" + v, true, mato3 == mato2);

        assertTrue("Tee luokalle Matopeli metodi public Omena getOmena()", _GRef.method("getOmena").returning(_ORef.cls()).takingNoParams().isPublic());

        v = ""
                + "Matopeli mp = Matopeli(10,10)\n"
                + "Omena o = mp.getOmena();\n";

        _O o = _GRef.method((_G) mp, "getOmena").returning(_ORef.cls()).takingNoParams().withNiceError(v).invoke();
        assertTrue("Palautettu omena oli null koodilla\n" + v, o != null);

        int x = _ORef.method(o, "getX").returning(int.class).takingNoParams().withNiceError(v).invoke();
        int y = _ORef.method(o, "getY").returning(int.class).takingNoParams().withNiceError(v).invoke();

        assertTrue("Omenan sijainti oli " + x + ", " + y + " koodilla:\n" + v, -1 < x && x < 10 && -1 < y && y < 10);

        assertTrue("Tee luokalle Matopeli metodi public void setOmena(Omena omena)", _GRef.method("setOmena").returningVoid().taking(_ORef.cls()).isPublic());

        v = ""
                + "Matopeli mp = Matopeli(10,10)\n"
                + "Omena o = new Omena(1,1);\n"
                + "mp.setOmena(m);\n";

        _O oSet = _ORef.constructor().taking(int.class, int.class).withNiceError(v).invoke(2, 2);
        _GRef.method((_G) mp, "setOmena").returningVoid().taking(_ORef.cls()).withNiceError(v).invoke(oSet);
        _O o2 = _GRef.method((_G) mp, "getOmena").returning(_ORef.cls()).takingNoParams().withNiceError(v).invoke();

        _GRef.method((_G) mp, "setOmena").returningVoid().taking(_ORef.cls()).withNiceError(v).invoke(oSet);
        assertEquals("Asettuuko uusi omena? Tarkasta koodi: " + v + "o == mp.getOmena()\n", true, o2 == oSet);

    }

    @Test
    @Points(Tehtava.ID + ".4")
    public void omenaEiMeneUlkopuolelleEikaMadonPaalle() throws Throwable {
        HashSet<Integer> xx = new HashSet<Integer>();
        HashSet<Integer> yy = new HashSet<Integer>();

        for (int i = 0; i < 200; i++) {
            String v = "Matopeli mp = new Matopeli(6,6);\n"
                    + "mp.getOmena();\n";
            Object mp = Reflex.reflect(matopeliClass).constructor().taking(int.class, int.class).invoke(6, 6);
            _M mato = _GRef.method((_G) mp, "getMato").returning(_MRef.cls()).takingNoParams().withNiceError(v).invoke();

            assertFalse("Palautettu mato ei saa olla null koodilla\n" + v, mato == null);

            assertEquals(v + "m.getPituus();\n", 1, (int) pituus(mato, v + "m.getPituus();"));

            assertEquals(v + "m.getSuunta();\n", Suunta.ALAS,
                    _MRef.method(mato, "getSuunta")
                            .returning(Suunta.class).takingNoParams().withNiceError(v + "m.getSuunta();\n").invoke());
            List<_P> ret = palat(mato, v + "m.getPalat();");

            List<_P> exp = new ArrayList<_P>();
            exp.add(newPala(3, 3));
            ret = palat(mato, v);
            assertTrue("Madon pitäisi olla alussa keskellä\n" + v + "m.getPalat();\n" + "palautit: " + ret, samat(exp, ret));

            _O o = _GRef.method((_G) mp, "getOmena").returning(_ORef.cls()).takingNoParams().withNiceError(v).invoke();
            assertTrue("Palautettu omena oli null koodilla\n" + v, o != null);

            int x = _ORef.method(o, "getX").returning(int.class).takingNoParams().withNiceError(v).invoke();
            int y = _ORef.method(o, "getY").returning(int.class).takingNoParams().withNiceError(v).invoke();
            xx.add(x);
            yy.add(y);

            assertFalse("Omenan sijainti oli " + x + "," + y + " eli madon päällä koodilla:\n" + v, x == 3 && y == 3);
            assertFalse("Omenan sijainti oli " + x + "," + y + " koodilla:\n" + v, 0 > x || x > 6 || 0 > y || y > 6);

        }

        assertFalse("Omena menee aina samaan alkupaikkaan kun \n"
                + "Matopeli mp = new Matopeli();", xx.size() == 1 && yy.size() == 1);

        assertTrue("Omena ei mene tarpeeksi satunnaiseen alkupaikkaan koodilla: \n"
                + "Matopeli mp = new Matopeli();", xx.size() == 6 && yy.size() == 6);
    }

//    /*
//     *
//     */
    @Points(Tehtava.ID + ".4")
    @Test
    public void paivitettava() throws Throwable {
        assertTrue("Onhan luokalla " + matopeliLuokka + " metodi public void paivita()?", Reflex.reflect(pakkaus + "." + matopeliLuokka).method("paivita").returningVoid().takingNoParams().isPublic());

        Object matopeli = Reflex.reflect(matopeliClass).constructor().taking(int.class, int.class).invoke(20, 10);

        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(matopeli);
    }

    @Points(Tehtava.ID + ".4")
    @Test
    public void paivittaminen() throws Throwable {
        int leveys = 20;
        int korkeus = 10;

        Object mp = Reflex.reflect(matopeliClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);

        _M mato = mato(mp);

        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);

        List<_P> palat = palat(mato, "");

        if (palat == null || palat.size() != 2 || !palat.get(0).getClass().equals(palaClass)) {
            fail("Liikutathan matoa matopeli-luokan paivita -metodissa? Lisääthän ensimmäisissä liikutuksissa myös paloja matoon.");
        }

        int x = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(palat.get(0));
        int tokaX = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(palat.get(1));
        assertTrue("Alaspäin liikkuvan madon x-koordinaattien ei pitäisi muuttua.", x == tokaX);
        int y = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(palat.get(0));
        int tokaY = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(palat.get(1));
        assertTrue("Alaspäin liikkuvan madon y-koordinaatin pitäisi kasvaa. Kahden palan pituisen madon y-koordinaattien eron tulee olla korkeintaan yksi.", Math.abs(y - tokaY) == 1);
    }

    public _M mato(Object mp) throws Throwable {
        return _GRef.method("getMato").returning(_MRef.cls()).takingNoParams().invokeOn((_G) mp);
    }

    public _O omena(Object al) throws Throwable {
        return _GRef.method("getOmena").returning(_ORef.cls()).takingNoParams().invokeOn((_G) al);
    }

    @Points(Tehtava.ID + ".4")
    @Test
    public void kunOmenaanEiOsutaSitaEiSyodaJaSenPaikkaEiMuutu() throws Throwable {
        int leveys = 20;
        int korkeus = 10;

        Object mp = Reflex.reflect(matopeliClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);

        _O omena = _ORef.constructor().taking(int.class, int.class).invoke(0, 0);
        int alkupX = (Integer) Reflex.reflect(omenaClass).method("getX").returning(int.class).takingNoParams().invokeOn(omena);
        int alkupY = (Integer) Reflex.reflect(omenaClass).method("getY").returning(int.class).takingNoParams().invokeOn(omena);

        _GRef.method("setOmena").returningVoid().taking(_ORef.cls()).invokeOn((_G) mp, omena);

        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);

        _M mato = mato(mp);
        List<_P> palat = palat(mato, "");

        if (palat == null || palat.size() != 2 || !palat.get(0).getClass().equals(palaClass)) {
            fail("Liikutathan matoa matopeli-luokan paivita -metodissa? Lisääthän ensimmäisissä liikutuksissa myös paloja matoon.");
        }

        _O haettuOmena = omena(mp);

        int x = getX(haettuOmena);
        int y = getY(haettuOmena);

        assertEquals("Kun matoa liikutetaan paivita-kutsussa ja se ei osu omenaan, "
                + "omenaa ei pitäisi syödä.", alkupX, x);
        assertEquals("Kun matoa liikutetaan paivita-kutsussa ja se ei osu omenaan, "
                + "omenaa ei pitäisi syödä.", alkupY, y);

    }

    @Points(Tehtava.ID + ".4")
    @Test
    public void kunOmenaanOsutaanSeSyodaanJaLuodaanUusiOmena() throws Throwable {
        int leveys = 10;
        int korkeus = 10;

        Object mp = Reflex.reflect(matopeliClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);

        _O omenax = _ORef.constructor().taking(int.class, int.class).invoke(0, 0);
        _GRef.method("setOmena").returningVoid().taking(_ORef.cls()).invokeOn((_G) mp, omenax);

        _M mato = mato(mp);

        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);

        List palat = palat(mato, "");
        if (palat == null || palat.size() < 3) {
            fail("Kahden liikkumiskerran jälkeen madon palojen määrä oli " + palat.size() + " vaikka sen pitäisi olla " + 3 + ", luodaanhan mato vain konstruktorissa?");
        }

        int palaX = -1;
        int palaY = -1;

        for (Object pala : palat) {
            int x = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(pala);
            int y = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(pala);

            if (y > palaY) {
                palaY = y;
            }

            if (x > palaX) {
                palaX = x;
            }
        }

        if (palaY <= 4) {
            fail("Kasvaahan madon y-koordinaatti sen alaspäin kulkiessa.");
        }

        _O omena = _ORef.constructor().taking(int.class, int.class).invoke(palaX, palaY + 1);
        _GRef.method("setOmena").returningVoid().taking(_ORef.cls()).invokeOn((_G) mp, omena);

        int alkupX = getX(omena);
        int alkupY = getY(omena);

        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);

        palat = palat(mato, "");

        if (palat == null || palat.size() != 4) {
            fail("Kun mato syö omenan, sen pituuden pitäisi olla kasvanut yhdellä seuraavan liiku-kutsun jälkeen.\n"
                    + ""
                    + kentta(palat, leveys, korkeus));
        }

        _O haettuOmena = omena(mp);

        int x = getX(haettuOmena);
        int y = getY(haettuOmena);

        assertTrue("Kun mato löytää omenan paivita-kutsussa, tulee omena syödä ja sille arpoa uusi sijanti. \n"
                + "Nyt sijainti säilyi samana\n"
                + "omena: " + x + "," + y + "\n"
                + kentta(palat, 10, 10)
                + "", alkupX != x || alkupY != y);

        assertTrue("Uusi omena ei saa tulla madon päälle:\n"
                + "omena: " + x + "," + y + "\n"
                + kentta(palat, 10, 10)
                + "", eiOsuMatoon(haettuOmena, palat));

        // ei törmäys
        boolean loppu = (Boolean) Reflex.reflect(matopeliClass).method("loppu").returning(boolean.class).takingNoParams().invokeOn(mp);
        assertFalse("Pelin tulee jatkua kun mato ei ole törmännyt itseensä. Varmista että metodi loppu() palauttaa arvon false kun peli on käynnissä.", loppu);
    }

    private String kentta(List<_P> palat, int x, int y) throws Throwable {
        char[][] t = new char[x][y];
        for (char[] cs : t) {
            for (int i = 0; i < cs.length; i++) {
                cs[i] = '_';
            }
        }
        for (_P p : palat) {
            t[gtY(p)][gtX(p)] = 'x';
        }
        t[gtY(palat.get(palat.size() - 1))][gtX(palat.get(palat.size() - 1))] = 'h';

        String mj = "";

        for (char[] cs : t) {
            for (char c : cs) {
                mj += c;
            }
            mj += "\n";
        }

        return mj;
    }

    @Points(Tehtava.ID + ".4")
    @Test
    public void uusiOmenaEiMeneMadonPaalle() throws Throwable {
        for (int i = 0; i < 10; i++) {
            int leveys = 5;
            int korkeus = 1;

            Object mp = Reflex.reflect(matopeliClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);

            _M mato = mato(mp);
            _O omena = _ORef.constructor().taking(int.class, int.class).invoke(3, 0);
            _GRef.method("setOmena").returningVoid().taking(_ORef.cls()).invokeOn((_G) mp, omena);
            Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
            _O haettuOmena = omena(mp);
            assertTrue("Uusi omena ei saa tulla madon päälle", getY(haettuOmena) != 2 && getY(haettuOmena) != 3);
        }

    }

    @Points(Tehtava.ID + ".4")
    @Test
    public void omenanSyominenKasvattaaMatoa() throws Throwable {
        int leveys = 20;
        int korkeus = 10;

        Object mp = Reflex.reflect(matopeliClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);

        _M mato = mato(mp);
        kaanny(mato, Suunta.OIKEA, "");

        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);

        List<_P> palat = palat(mato, pakkaus);
        if (palat == null || palat.size() < 3) {
            fail("Kolmen liikkumiskerran jälkeen madon palojen määrä oli " + palat.size() + ", luodaanhan mato vain konstruktorissa?");
        }

        int matoX = -1;
        int matoY = -1;

        for (_P pala : palat) {
            int x = gtX(pala);
            int y = gtY(pala);

            if (y > matoY) {
                matoY = y;
            }

            if (x > matoX) {
                matoX = x;
            }
        }

        Reflex.reflect(matopeliClass).method("setOmena").returningVoid().taking(omenaClass).invokeOn(mp, Reflex.reflect(omenaClass).constructor().taking(int.class, int.class).invoke(matoX + 1, matoY));

        int uusiMatoX = -1;
        int uusiMatoY = -1;

        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);

        if (palat == null || palat.size() != 4) {
            fail("Kun mato löytää paivita-metodissa omenan, tulee sen pituuden kasvaa yhdellä. Kun kolmen palan pituinen mato söi omenan, ja sen liiku-metodia kutsuttiin, sen pituuden olisi pitänyt olla 4. Nyt oli " + palat.size());
        }

        mato = mato(mp);
        palat = palat(mato, "");

        if (palat == null || palat.size() != 4) {
            fail("Kun mato löytää paivita-metodissa omenan, tulee sen pituuden kasvaa yhdellä. Kun kolmen palan pituinen mato söi omenan, ja sen liiku-metodia kutsuttiin, sen pituuden olisi pitänyt olla 4. Nyt oli " + palat.size());
        }

        for (Object pala : palat) {
            int x = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(pala);
            int y = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(pala);

            if (y > uusiMatoY) {
                uusiMatoY = y;
            }

            if (x > uusiMatoX) {
                uusiMatoX = x;
            }
        }

        assertEquals("Kun mato liikkuu oikealle, ei sen y-koordinaatin tule muuttua.", uusiMatoY, matoY);
        assertEquals("Kun mato liikkuu oikealle kaksi kertaa, tulee sen x-koordinaatin kasvaa kahdella.", uusiMatoX, matoX + 2);

        Reflex.reflect(matopeliClass).method("setOmena").returningVoid().taking(omenaClass).invokeOn(mp, Reflex.reflect(omenaClass).constructor().taking(int.class, int.class).invoke(uusiMatoX + 1, matoY));

        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);

        mato = mato(mp);
        palat = (List) Reflex.reflect(matoClass).method("getPalat").returning(List.class).takingNoParams().invokeOn(mato);

        if (palat == null || palat.size() != 5) {
            fail("Kun mato löytää paivita-metodissa omenan, tulee sen pituuden kasvaa yhdellä. Kun neljän palan pituinen mato söi omenan, ja sen liiku-metodia kutsuttiin, sen pituuden olisi pitänyt olla 5. Nyt oli " + palat.size());
        }

    }

    @Points(Tehtava.ID + ".4")
    @Test
    public void tormaaminenItseensaLopettaa1() throws Throwable {
        int leveys = 6;
        int korkeus = 6;

        Object mp = Reflex.reflect(matopeliClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);

        _M mato = mato(mp);

        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        kaanny(mato, Suunta.YLOS, "");
        String k = kentta(palat(mato, ""), leveys, korkeus);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        boolean loppu = (Boolean) Reflex.reflect(matopeliClass).method("loppu").returning(boolean.class).takingNoParams().invokeOn(mp);
        assertTrue("Pelin tulee loppua, eli metodin loppu() tulee palauttaa true, kun mato törmää itseensä. \nVarmista että mato voi törmätä itseensä siten, että se on ensin liikkunut alas, ja liikkuu sen jälkeen heti ylös tulosuuntaansa kohti.\n"
                + "YLOS\n" + k + "", loppu);

    }

    @Points(Tehtava.ID + ".4")
    @Test
    public void tormaaminenItseensaLopettaa2() throws Throwable {
        int leveys = 10;
        int korkeus = 10;

        Object mp = Reflex.reflect(matopeliClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);

        _M mato = mato(mp);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        uusiOmena(mp, 5, 8);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        uusiOmena(mp, 5, 9);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        kaanny(mato, Suunta.OIKEA, "");
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        kaanny(mato, Suunta.YLOS, "");
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        kaanny(mato, Suunta.VASEN, "");
        String k = kentta(palat(mato, ""), leveys, korkeus);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);

        assertTrue("Pelin tulee loppua, eli metodin loppu() tulee palauttaa true, kun mato törmää itseensä. \n"
                + "VASEN\n"
                + k, peliLoppuu(mp));

    }

    @Points(Tehtava.ID + ".4")
    @Test
    public void tormaaminenlaitaanLopettaa1() throws Throwable {
        int leveys = 4;
        int korkeus = 4;

        Object mp = Reflex.reflect(matopeliClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);

        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        _M mato = mato(mp);
        String k = kentta(palat(mato, ""), leveys, korkeus);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        assertTrue("Pelin tulee loppua, eli metodin loppu() tulee palauttaa true, kun mato törmää laitaan. \n"
                + "ALAS\n"
                + k, peliLoppuu(mp));
    }

    @Points(Tehtava.ID + ".4")
    @Test
    public void tormaaminenlaitaanLopettaa2() throws Throwable {
        int leveys = 4;
        int korkeus = 4;

        Object mp = Reflex.reflect(matopeliClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);

        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        _M mato = mato(mp);
        kaanny(mato, Suunta.OIKEA, "");
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        String k = kentta(palat(mato, ""), leveys, korkeus);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        assertTrue("Pelin tulee loppua, eli metodin loppu() tulee palauttaa true, kun mato törmää laitaan. \n"
                + "OIKEA\n"
                + k, peliLoppuu(mp));
    }

    @Points(Tehtava.ID + ".4")
    @Test
    public void tormaaminenlaitaanLopettaa3() throws Throwable {
        int leveys = 4;
        int korkeus = 4;

        Object mp = Reflex.reflect(matopeliClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);

        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        _M mato = mato(mp);
        kaanny(mato, Suunta.VASEN, "");
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        String k = kentta(palat(mato, ""), leveys, korkeus);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        assertTrue("Pelin tulee loppua, eli metodin loppu() tulee palauttaa true, kun mato törmää laitaan. \n"
                + "VASEN\n"
                + k, peliLoppuu(mp));
    }

    @Points(Tehtava.ID + ".4")
    @Test
    public void tormaaminenlaitaanLopettaa4() throws Throwable {
        int leveys = 4;
        int korkeus = 4;

        Object mp = Reflex.reflect(matopeliClass).constructor().taking(int.class, int.class).invoke(leveys, korkeus);

        _M mato = mato(mp);
        kaanny(mato, Suunta.VASEN, "");
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        kaanny(mato, Suunta.YLOS, "");
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        String k = kentta(palat(mato, ""), leveys, korkeus);
        Reflex.reflect(matopeliClass).method("paivita").returningVoid().takingNoParams().invokeOn(mp);
        assertTrue("Pelin tulee loppua, eli metodin loppu() tulee palauttaa true, kun mato törmää laitaan. \n"
                + "YLOS\n"
                + k, peliLoppuu(mp));
    }

    /*
     * TODO: langotus
     *
     *
     */
    private String f(List<Point> points) {
        String m = "";

        for (Point point : points) {
            m += " (" + point.x + "," + point.y + ") ";
        }

        return m;
    }

    private String ff(List<Rectangle> rects) {
        String m = "";

        for (Rectangle rect : rects) {
            m += " g.draw3DRectangle(" + rect.x + "," + rect.y + "," + rect.width + "," + rect.height + ", true);\n";
        }

        return m;
    }

    private boolean peliLoppuu(Object mp) throws Throwable {
        return (Boolean) Reflex.reflect(matopeliClass).method("loppu").returning(boolean.class).takingNoParams().invokeOn(mp);
    }

    public void uusiOmena(Object mp, int x, int y) throws Throwable {
        _GRef.method("setOmena").returningVoid().taking(omenaClass).invokeOn((_G) mp, Reflex.reflect(omenaClass).constructor().taking(int.class, int.class).invoke(x, y));
    }

    private boolean eiOsuMatoon(_O o, List<_P> p) throws Throwable {
        for (_P _p : p) {
            if (gtX(_p) == getX(o) && gtY(_p) == getY(o)) {
                return false;
            }
        }

        return true;
    }

    private int gtX(_P o) throws Throwable {
        return _PRef.method(o, "getX").returning(int.class).takingNoParams().invoke();
    }

    private int gtY(_P o) throws Throwable {
        return _PRef.method(o, "getY").returning(int.class).takingNoParams().invoke();
    }

    private int getX(_O o) throws Throwable {
        return _ORef.method(o, "getX").returning(int.class).takingNoParams().invoke();
    }

    private int getY(_O o) throws Throwable {
        return _ORef.method(o, "getY").returning(int.class).takingNoParams().invoke();
    }

    private void liikuYmpyra(_M mato, int origX, int origY) throws Throwable {
        String v = "Mato = new Mato(1,1,Suunta.OIKEA);\n"
                + "mato.liiku();\n"
                + "mato.liiku();\n'"
                + "mato.liiku();\n"
                + "mato.kasva();\n"
                + "mato.liiku();\n";

        liiku(mato, v);
        liiku(mato, v);
        liiku(mato, v);
        kasva(mato, v);
        liiku(mato, v);

        int pituus = pituus(mato, "");
        assertEquals(v + "mato.getPituus();\n", 4, pituus);

        liiku(mato, v);

        v += "mato.liiku();\n";
        pituus = pituus(mato, "");
        assertEquals(v + "mato.getPituus();\n", 4, pituus);

        List palat = palat(mato, v);

        if (palat == null || palat.size() != 4 || !palat.get(0).getClass().equals(palaClass)) {
            fail("Kun madon pituus on 4, tulee metodin getPalat palauttaa lista, jossa on 4 Pala-tyyppistä oliota.");
        }

        for (Object p : palat) {
            int y = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(p);
            assertTrue("Kun palan suunta on Suunta.OIKEA, ei sen y-akselin arvon pitäisi muuttua liikuttaessa.", y == 1);
            int x = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(p);
            assertTrue("Kun palan suunta on Suunta.OIKEA, sen x-akselin arvojen tulee kasvaa liikuttaessa.", x > 1);
        }

        v += "mato.setSuunta(Suunta.ALAS);\n";

        kaanny(mato, Suunta.ALAS, v);

        v += "5 kertaa: mato.liiku();\n";

        liiku(mato, v);
        liiku(mato, v);
        liiku(mato, v);
        liiku(mato, v);
        liiku(mato, v);

        palat = palat(mato, v);

        int alkupX = -100;
        for (Object p : palat) {
            int y = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(p);
            assertTrue("Kun madon suunta on Suunta.ALAS, tulee sen y-akselin arvojen kasvaa alas mentäessä.", y > 1);
            int x = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(p);
            if (alkupX == -100) {
                alkupX = x;
                continue;
            }

            assertTrue("Kun madon suunta on Suunta.ALAS, sen x-akselin arvojen tulee pysyä samana liikuttaessa.", x == alkupX);
        }

        v += "mato.setSuunta(Suunta.VASEN);\n";

        kaanny(mato, Suunta.VASEN, v);

        v += "5 kertaa: mato.liiku();\n";

        liiku(mato, v);
        liiku(mato, v);
        liiku(mato, v);
        liiku(mato, v);
        liiku(mato, v);

        palat = (List) _MRef.method("getPalat").returning(List.class).takingNoParams().invokeOn(mato);

        int alkupY = -100;
        for (Object p : palat) {
            int y = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(p);
            if (alkupY == -100) {
                alkupY = y;
                continue;
            }
            assertTrue("Kun madon suunta on Suunta.VASEN, tulee sen y-akselin arvojen pysyä samana.", y == alkupY);
            int x = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(p);

            assertTrue("Kun madon suunta on Suunta.VASEN, sen x-akselin arvojen tulee pienentyä liikuttaessa.", x < 5);
        }

        v += "mato.setSuunta(Suunta.YLOS);\n";

        kaanny(mato, Suunta.YLOS, v);

        v += "5 kertaa: mato.liiku();\n";

        liiku(mato, v);
        liiku(mato, v);
        liiku(mato, v);
        liiku(mato, v);
        liiku(mato, v);

        palat = (List) _MRef.method("getPalat").returning(List.class).takingNoParams().invokeOn(mato);

        boolean jokuAlkupisteessa = false;
        for (Object p : palat) {
            int y = (Integer) Reflex.reflect(palaClass).method("getY").returning(int.class).takingNoParams().invokeOn(p);
            assertTrue("Kun madon suunta on Suunta.YLOS, tulee sen y-akselin arvojen pienentyä.", y < 5);
            int x = (Integer) Reflex.reflect(palaClass).method("getX").returning(int.class).takingNoParams().invokeOn(p);
            assertTrue("Kun madon suunta on Suunta.YLOS, sen x-akselin arvojen tulee pysyä samana.", x < 5);

            if (x == origX && y == origY) {
                jokuAlkupisteessa = true;
            }
        }

        if (!jokuAlkupisteessa) {
            fail("Kun liikutaan 5 kertaa jokaiseen ilmansuuntaan, madon pitäisi päätyä takaisin alkupisteeseen.");
        }

    }

    private _P newPala(int x, int y) throws Throwable {
        _P toinenKakkosNelonen = _PRef.constructor().taking(int.class, int.class).invoke(x, y);
        return toinenKakkosNelonen;
    }

    private boolean samat(List<_P> ex, List<_P> was) throws Throwable {
        if (ex.size() != was.size()) {
            return false;
        }

        for (int i = 0; i < was.size(); i++) {
            _P p1 = ex.get(i);
            _P p2 = was.get(i);

            int p1x = _PRef.method("getX").returning(int.class).takingNoParams().invokeOn(p1);
            int p2x = _PRef.method("getX").returning(int.class).takingNoParams().invokeOn(p2);
            int p1y = _PRef.method("getY").returning(int.class).takingNoParams().invokeOn(p1);
            int p2y = _PRef.method("getY").returning(int.class).takingNoParams().invokeOn(p2);

            if (p1 == null || p2 == null) {
                return false;
            }
            if (p1x != p2x) {
                return false;
            }
            if (p1y != p2y) {
                return false;
            }
        }

        return true;
    }

    private List<_P> palat(_M mato, String v) throws Throwable {
        List<_P> ret;
        ret = _MRef.method(mato, "getPalat").returning(List.class).takingNoParams().withNiceError(v).invoke();
        return ret;
    }

    private Integer pituus(_M mato, String v) throws Throwable {
        return _MRef.method(mato, "getPituus").returning(int.class).takingNoParams().withNiceError(v).invoke();
    }

    private _M newMato(int x, int y, Suunta s, String v) throws Throwable {
        return _MRef.constructor().taking(int.class, int.class, Suunta.class).withNiceError(v).invoke(x, y, s);
    }

    private void liiku(_M mato, String v) throws Throwable {
        _MRef.method(mato, "liiku").returningVoid().takingNoParams().withNiceError(v).invoke();
    }

    private void kaanny(_M mato, Suunta s, String v) throws Throwable {
        _MRef.method(mato, "setSuunta").returningVoid().taking(Suunta.class).withNiceError(v).invoke(s);
    }

    private void kasva(_M mato, String v) throws Throwable {
        _MRef.method(mato, "kasva").returningVoid().takingNoParams().withNiceError(v).invoke();
    }

    private void saniteettitarkastus(String klassName, int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("et tarvitse \"stattisia muuttujia\", poista luokalta " + s(klassName) + " muuttuja " + kentta(field.toString(), s(klassName)), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("luokan kaikkien oliomuuttujien näkyvyyden tulee olla private, muuta luokalta " + s(klassName) + " löytyi: " + kentta(field.toString(), klassName), field.toString().contains("private"));
        }

        if (kentat.length > 1) {
            int var = 0;
            for (Field field : kentat) {
                if (!field.toString().contains("final")) {
                    var++;
                }
            }
            assertTrue("et tarvitse " + s(klassName) + "-luokalle kuin " + m + ", poista ylimääräiset", var <= n);
        }
    }

    private String kentta(String toString, String klassName) {
        return toString.replace(klassName + ".", "").replace("java.lang.", "").replace("java.util.", "");
    }

    private String s(String klassName) {
        return klassName.substring(klassName.lastIndexOf(".") + 1);
    }

    private boolean sis(List<_P> exp, _P p) throws Throwable {
        for (_P _p : exp) {
            if (sama(_p, p)) {
                return true;
            }
        }

        return false;
    }

    private boolean sama(_P p1, _P p2) throws Throwable {
        int p1x = _PRef.method("getX").returning(int.class).takingNoParams().invokeOn(p1);
        int p2x = _PRef.method("getX").returning(int.class).takingNoParams().invokeOn(p2);
        int p1y = _PRef.method("getY").returning(int.class).takingNoParams().invokeOn(p1);
        int p2y = _PRef.method("getY").returning(int.class).takingNoParams().invokeOn(p2);

        if (p1 == null || p2 == null) {
            return false;
        }
        if (p1x != p2x) {
            return false;
        }
        if (p1y != p2y) {
            return false;
        }

        return true;
    }

    private Boolean osuuItseensa(_M m, String v) throws Throwable {
        return _MRef.method(m, "osuuItseensa").returning(boolean.class).takingNoParams().withNiceError(v).invoke();
    }

}
