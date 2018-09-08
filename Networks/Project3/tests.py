import unittest
from Topology import Topology
from helpers import finish_log, open_log


class UnitTests(unittest.TestCase):
    @staticmethod
    def _parse(array_of_strings):
        return {
            string.split(':')[0].strip(): set(map(str.strip, string.split(':')[1].split(',')))
            for string in array_of_strings
        }

    @staticmethod
    def _read_log(log_file):
        lines_to_parse = []
        purge_previous = False

        with open(log_file) as f:
            for line in f:
                if '-----' in line:
                    purge_previous = True
                else:
                    if purge_previous:
                        lines_to_parse = []
                        purge_previous = False

                    lines_to_parse.append(line)

        return lines_to_parse

    def _run(self, input_):
        log_file = "{}.log".format(input_)
        open_log(log_file)
        topology = Topology("{}.txt".format(input_))
        topology.run_topo()
        finish_log()
        log = self._read_log(log_file)
        return self._parse(log)

    def test_simple_topo(self):
        actual = self._run("SimpleTopo")
        expected = self._parse("A:A0,C3,B1,D3 B:A1,C2,B0,D2 C:A3,C0,B2,D0 D:A3,C0,B2,D0 E:A2,C-1,B1,E0,D-1".split())
        self.assertEqual(expected, actual)

    def test_single_loop_topo(self):
        actual = self._run("SingleLoopTopo")
        expected = self._parse("A:A0,C16,B6,E6,D5 B:A2,C10,B0,E0,D7 C:C0 D:A3,C11,B1,E1,D0 E:A2,C10,B0,E0,D7".split())
        self.assertEqual(expected, actual)

    def test_simple_negative_cycle(self):
        actual = self._run("SimpleNegativeCycle")
        expected = self._parse("AA:AA0,CC-99,AB0,AE-1,AD-2 AB:AA-1,CC-99,AB0,AE-2,AD-3 AD:AA1,CC-99,AB2,AE1,AD0 AE:AA0,CC-99,AB1,AE0,AD-2 CC:CC0,AA-1,AB0,AE-2,AD-3".split())
        self.assertEqual(expected, actual)

    def test_complex_topo(self):
        actual = self._run("ComplexTopo")
        expected = self._parse("ATT:TWC-99,GSAT-8,UGA-99,ATT0,VZ-3,CMCT-99,VONA-11 CMCT:TWC-99,GSAT-7,UGA-99,ATT1,VZ-2,CMCT0,VONA-10 DRPA:TWC-99,GT-1,GSAT5,UGA-99,PTGN1,OSU-1,ATT13,VONA2,EGLN1,VZ10,DRPA0,CMCT-99,UC-1 EGLN:TWC-99,GT-2,GSAT5,UGA-99,PTGN0,OSU-2,ATT13,VONA3,EGLN0,VZ11,DRPA1,CMCT-99,UC-2 GSAT:TWC-99,GSAT0,UGA-99,ATT7,VZ5,CMCT-99,VONA-3 GT:TWC-99,GT0,GSAT7,UGA-99,PTGN2,OSU0,ATT15,VONA5,EGLN2,VZ13,DRPA3,CMCT-99,UC0 OSU:TWC-99,GT0,GSAT7,UGA-99,PTGN2,OSU0,ATT15,VONA5,EGLN2,VZ13,DRPA3,CMCT-99,UC0 PTGN:TWC-99,GT-1,GSAT5,UGA-99,PTGN0,OSU-1,ATT13,VONA3,EGLN1,VZ11,DRPA2,CMCT-99,UC-1 TWC:TWC0,GSAT-7,UGA-99,ATT1,VZ-2,CMCT-99,VONA-10 UC:TWC-99,GT0,GSAT7,UGA-99,PTGN2,OSU0,ATT15,VONA5,EGLN2,VZ13,DRPA3,CMCT-99,UC0 UGA:TWC-99,GSAT42,UGA0,ATT50,VZ47,CMCT-99,VONA39 VONA:TWC-99,GSAT2,UGA-99,ATT10,VZ8,CMCT-99,VONA0 VZ:TWC-99,GSAT-6,UGA-99,ATT2,VZ0,CMCT-99,VONA-9".split())
        self.assertEqual(expected, actual)

    def test_double_negative_loop_topo(self):
        actual = self._run("DoubleNegativeLoopTopo")
        expected = self._parse("A:A0,C-20,B-10,E-99,D-99,G-99,F-99 B:A-99,C-10,B0,E-99,D-99,G-99,F-99 C:A-99,C0,B-99,E-99,D-99,G-99,F-99 D:A-99,C-99,B-99,E-99,D0,G-99,F-99 E:A-99,C-99,B-99,E0,D-99,G-99,F-99 F:A-99,C-99,B-99,E-20,D-99,G-10,F0 G:A-99,C-99,B-99,E-10,D-99,G0,F-99".split())
        self.assertEqual(expected, actual)

    def test_route_map_topo(self):
        actual = self._run("RouteMapTopo")
        expected = self._parse("""AMS:AMS0
ATL:JFK-99,HND-99,ATL0,IST-99,KUL-99,DEL-99,CAN-99,PEK-10,CGK-99,BKK-99,PVG-99,AMS-99,HKG-99,FRA-99,ICN-99,DFW-99,LHR-99,CLT-99,CDG-99,SFO-99,LAX-99,ORD-99,DXB-99,LAS-99
BKK:JFK-99,HND-99,ATL-99,IST-99,KUL-99,DEL-99,CAN-99,PEK-9,CGK-99,BKK0,PVG-99,AMS-99,HKG10,FRA-99,ICN-99,DFW-99,LHR-99,CLT-99,CDG-99,SFO-99,LAX-99,ORD-99,DXB1,LAS-99
CAN:FRA-15,DEL-5,CAN0,AMS-99
CDG:JFK-99,HND-99,ATL-99,IST-99,KUL-99,DEL-99,CAN-99,PEK-4,CGK-99,BKK-99,PVG-99,AMS-99,HKG-99,FRA-99,ICN-99,DFW-99,LHR-99,CLT-99,CDG0,SFO-99,LAX-99,ORD-99,DXB-99,LAS-99
CGK:JFK-99,HND-99,ATL-99,IST-99,KUL-99,DEL-99,CAN-99,PEK-6,CGK0,BKK3,PVG-99,AMS-99,HKG13,FRA-99,ICN-99,DFW-99,LHR-99,CLT-99,CDG-99,SFO-99,LAX-99,ORD-99,DXB4,LAS-99
CLT:JFK-99,HND-99,ATL-10,IST-99,KUL-99,DEL-99,CAN-99,PEK-20,CGK-99,BKK-99,PVG-99,AMS-99,HKG-99,FRA-99,ICN-99,DFW-30,LHR-99,CLT0,CDG-99,SFO-99,LAX-99,ORD-20,DXB-99,LAS-99
DEL:FRA-25,DEL0,CAN-10,AMS-99
DEN:JFK-99,HND-99,ATL50,IST-99,KUL-99,DEL-99,CAN-99,DEN0,PEK40,CGK-99,BKK-99,PVG-99,AMS-99,HKG-99,FRA-99,ICN-99,DFW30,LHR-99,CLT-99,CDG-99,SFO-99,LAX-99,ORD40,DXB-99,LAS10
DFW:JFK-99,HND-99,ATL20,IST-99,KUL-99,DEL-99,CAN-99,PEK10,CGK-99,BKK-99,PVG-99,AMS-99,HKG-99,FRA-99,ICN-99,DFW0,LHR-99,CLT-99,CDG-99,SFO-99,LAX-99,ORD10,DXB-99,LAS-99
DXB:JFK-99,HND-99,ATL-99,IST-99,KUL-99,DEL-99,CAN-99,PEK-10,CGK-99,BKK-99,PVG-99,AMS-99,HKG-99,FRA-99,ICN-99,DFW-99,LHR-99,CLT-99,CDG-99,SFO-99,LAX-99,ORD-99,DXB0,LAS-99
FRA:FRA0,DEL10,AMS-99,CAN0
HKG:JFK-99,HND-99,ATL-99,IST-99,KUL-99,DEL-99,CAN-99,PEK-19,CGK-99,BKK-99,PVG-99,AMS-99,HKG0,FRA-99,ICN-99,DFW-99,LHR-99,CLT-99,CDG-99,SFO-99,LAX-99,ORD-99,DXB-9,LAS-99
HND:JFK-99,HND0,ATL-99,IST-99,KUL-99,DEL-99,CAN-99,PEK-14,CGK-99,BKK-99,PVG-99,AMS-99,HKG5,FRA-99,ICN-99,DFW-99,LHR-99,CLT-99,CDG-99,SFO-99,LAX-99,ORD-99,DXB-4,LAS-99
IAH:JFK-99,HND-99,ATL-30,IST-99,KUL-99,DEL-99,CAN-99,PEK-40,ICN-99,CGK-99,BKK-99,PVG-99,AMS-99,HKG-99,FRA-99,IAH0,DFW-50,LHR-99,CLT-99,CDG-99,SFO-99,LAX-99,ORD-40,DXB-99,LAS-99
ICN:FRA-99,ICN0,DEL-99,CAN-99,PVG-10,AMS-99
IST:JFK-99,HND-99,ATL-99,IST0,KUL-99,DEL-99,CAN-99,PEK-10,CGK-99,BKK-99,PVG-99,AMS-99,HKG-99,FRA-99,ICN-99,DFW-99,LHR-99,CLT-99,CDG-99,SFO-99,LAX-99,ORD-99,DXB-99,LAS-99
JFK:JFK0,HND-99,ATL10,IST-99,KUL-99,DEL-99,CAN-99,PEK0,CGK-99,BKK-99,PVG-99,AMS-99,HKG-99,FRA-99,ICN-99,DFW-99,LHR-99,CLT-99,CDG-99,SFO-99,LAX-99,ORD-99,DXB-99,LAS-99
KUL:JFK-99,HND-99,ATL-99,IST-99,KUL0,DEL-99,CAN-99,PEK4,CGK10,BKK13,PVG-99,AMS-99,HKG23,FRA-99,ICN-99,DFW-99,LHR-99,CLT-99,CDG-99,SFO-99,LAX-99,ORD-99,DXB14,LAS-99
LAS:JFK-99,HND-99,ATL40,IST-99,KUL-99,DEL-99,CAN-99,PEK30,CGK-99,BKK-99,PVG-99,AMS-99,HKG-99,FRA-99,ICN-99,DFW20,LHR-99,CLT-99,CDG-99,SFO-99,LAX-99,ORD30,DXB-99,LAS0
LAX:SFO10,LAX0
LHR:JFK-99,HND-99,ATL-99,IST-99,KUL-99,DEL-99,CAN-99,PEK6,CGK-99,BKK-99,PVG-99,AMS-99,HKG-99,FRA-99,ICN-99,DFW-99,LHR0,CLT-99,CDG10,SFO-99,LAX-99,ORD-99,DXB-99,LAS-99
MAD:JFK-99,HND-99,ATL-99,IST-99,KUL-99,DEL-99,CAN-99,PEK-4,CGK-99,BKK-99,PVG-99,AMS-99,HKG-99,FRA-99,ICN-99,DFW-99,LHR-10,CLT-99,CDG0,SFO-99,LAX-99,MAD0,ORD-99,DXB-99,LAS-99
MIA:JFK-99,HND-99,MIA0,ATL10,IST-99,KUL-99,DEL-99,CAN-99,PEK0,CGK-99,BKK-99,PVG-99,AMS-99,HKG-99,FRA-99,ICN-99,DFW-99,LHR-99,CLT-99,CDG-99,SFO-99,LAX-99,ORD-99,DXB-99,LAS-99
ORD:JFK-99,HND-99,ATL10,IST-99,KUL-99,DEL-99,CAN-99,PEK0,CGK-99,BKK-99,PVG-99,AMS-99,HKG-99,FRA-99,ICN-99,DFW-99,LHR-99,CLT-99,CDG-99,SFO-99,LAX-99,ORD0,DXB-99,LAS-99
PEK:JFK-99,HND-99,ATL-99,IST-99,KUL-99,DEL-99,CAN-99,PEK0,CGK-99,BKK-99,PVG-99,AMS-99,HKG-99,FRA-99,ICN-99,DFW-99,LHR-99,CLT-99,CDG-99,SFO-99,LAX-99,ORD-99,DXB-99,LAS-99
PHX:JFK-99,HND-99,ATL-36,IST-99,KUL-99,DEL-99,CAN-99,DEN7,PEK-46,ICN-99,CGK-99,BKK-99,PVG-99,AMS-99,HKG-99,FRA-99,IAH-6,DFW-56,LHR-99,CLT-99,CDG-99,SFO-99,PHX0,LAX-99,ORD-46,DXB-99,LAS-99
PVG:FRA-99,ICN5,DEL-99,CAN-99,PVG0,AMS-99
SFO:SFO0,LAX10
SIN:JFK-99,HND-99,ATL-99,IST-99,KUL11,DEL-99,CAN-99,PEK15,SIN0,CGK21,BKK24,PVG-99,AMS-99,HKG34,FRA-99,ICN-99,DFW-99,LHR-99,CLT-99,CDG-99,SFO-99,LAX-99,ORD-99,DXB25,LAS-99""".split())
        self.assertEqual(expected, actual)

    def test_genc_kastrati_topo(self):
        actual = self._run("GencKastratiTopo")
        expected = self._parse("""A:A0
B:A-99,CC-9,B0,G-99,HK-3
CC:CC0,A-99,B-99,G-99,HK-8
D:A-99,B-99,D0,G-99,CC-2,HK-10
G:CC-5,HK3,B-99,A-99,G0
HK:CC-8,HK0,B-99,A-99,G-99""".split())
        self.assertEqual(expected, actual)

    def test_negative_50_loop(self):
        actual = self._run("Negative50Loop")
        expected = self._parse("""A:A0,B-50,C-99
B:B0,C-50,A-99
C:C0,A-50,B-99""".split())
        self.assertEqual(expected, actual)

if __name__ == '__main__':
    unittest.main()
