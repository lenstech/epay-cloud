package com.lens.epay.service;

import com.lens.epay.mapper.OrderMapper;
import com.lens.epay.model.dto.sale.OrderDto;
import com.lens.epay.model.entity.*;
import com.lens.epay.repository.FirmRepository;
import com.lens.epay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 11 May 2020
 */
@Service
public class AgreementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FirmRepository firmRepository;

    @Autowired
    private OrderMapper orderMapper;

    public String getSalesAgreement(UUID userId, OrderDto orderDto) {

        Order order = orderMapper.toEntity(orderDto);

        User user = userRepository.findUserById(userId);

        Firm firm = firmRepository.findByName("default firm");
        //todo firma düzeltilecek

        Address deliveryAddress = order.getDeliveryAddress();

        Address invoiceAddress = order.getInvoiceAddress();

        StringBuilder productPart = new StringBuilder();

        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, new Locale("tr"));

        for (BasketObject object : order.getBasketObjects()) {
            productPart.append("Ürün Açıklaması: ").append(object.getProduct().getName()).append("\n")
                    .append("Adet: ").append(object.getProductQuantity()).append("\n")
                    .append("Birim: ").append(object.getProduct().getUnit().getTurkish()).append("\n")
                    .append("Fiyatı(₺): ").append(object.getProduct().getPrice()).append("\n");
        }

        return "MESAFELİ SATIŞ SÖZLEŞMESİ\n" +
                "1.TARAFLAR\n" +
                "İşbu Sözleşme aşağıdaki taraflar arasında aşağıda belirtilen hüküm ve şartlar çerçevesinde imzalanmıştır. \n" +
                "A.‘ALICI’ ; (sözleşmede bundan sonra \"ALICI\" olarak anılacaktır) " + user.getName() + " " + user.getSurname() + "\n" +
                "B.‘SATICI’ ; (sözleşmede bundan sonra \"SATICI\" olarak anılacaktır)\n" +
                "AD- SOYAD: " + firm.getName() + "\n" +
                "ADRES: " + firm.getAddress() + "\n" +
                "İş bu sözleşmeyi kabul etmekle ALICI, sözleşme konusu siparişi onayladığı takdirde sipariş konusu bedeli ve varsa kargo ücreti, vergi gibi belirtilen ek ücretleri ödeme yükümlülüğü altına gireceğini ve bu konuda bilgilendirildiğini peşinen kabul eder.\n" +
                "\n" +
                "\n" +
                "2.TANIMLAR\n" +
                "İşbu sözleşmenin uygulanmasında ve yorumlanmasında aşağıda yazılı terimler karşılarındaki yazılı açıklamaları ifade edeceklerdir.\n" +
                "BAKAN: Gümrük ve Ticaret Bakanı’nı,\n" +
                "BAKANLIK: Gümrük ve Ticaret  Bakanlığı’nı,\n" +
                "KANUN: 6502 sayılı Tüketicinin Korunması Hakkında Kanun’u,\n" +
                "YÖNETMELİK: Mesafeli Sözleşmeler Yönetmeliği’ni (RG:27.11.2014/29188)\n" +
                "HİZMET: Bir ücret veya menfaat karşılığında yapılan ya da yapılması taahhüt edilen mal sağlama dışındaki her türlü tüketici işleminin konusunu ,\n" +
                "SATICI: Ticari veya mesleki faaliyetleri kapsamında tüketiciye mal sunan veya mal sunan adına veya hesabına hareket eden şirketi,\n" +
                "ALICI: Bir mal veya hizmeti ticari veya mesleki olmayan amaçlarla edinen, kullanan veya yararlanan gerçek ya da tüzel kişiyi,\n" +
                "SİTE: SATICI’ya ait internet sitesini,\n" +
                "SİPARİŞ VEREN: Bir mal veya hizmeti SATICI’ya ait internet sitesi üzerinden talep eden gerçek ya da tüzel kişiyi,\n" +
                "TARAFLAR: SATICI ve ALICI’yı,\n" +
                "SÖZLEŞME: SATICI ve ALICI arasında akdedilen işbu sözleşmeyi,\n" +
                "MAL: Alışverişe konu olan taşınır eşyayı ve elektronik ortamda kullanılmak üzere hazırlanan yazılım, ses, görüntü ve benzeri gayri maddi malları ifade eder.\n" +
                "\n" +
                "\n" +
                "3.KONU\n" +
                "İşbu Sözleşme, ALICI’nın, SATICI’ya ait internet sitesi üzerinden elektronik ortamda siparişini verdiği aşağıda nitelikleri ve satış fiyatı belirtilen ürünün satışı ve teslimi ile ilgili olarak 6502 sayılı Tüketicinin Korunması Hakkında Kanun ve Mesafeli Sözleşmelere Dair Yönetmelik hükümleri gereğince tarafların hak ve yükümlülüklerini düzenler.\n" +
                "Listelenen ve sitede ilan edilen fiyatlar satış fiyatıdır. İlan edilen fiyatlar ve vaatler güncelleme yapılana ve değiştirilene kadar geçerlidir. Süreli olarak ilan edilen fiyatlar ise belirtilen süre sonuna kadar geçerlidir.\n" +
                "\n" +
                "\n" +
                "4. SATICI BİLGİLERİ\n" +
                "Ünvanı: " + firm.getName() + "\n" +
                "Adres: " + firm.getAddress() + "\n" +
                "Telefon: " + firm.getPhoneNo() + "\n" +
                "Faks: " + firm.getPhoneNo() + "\n" +
                "Eposta: " + firm.getEmail() + "\n" +
                "\n" +
                "\n" +
                "5. ALICI BİLGİLERİ\n" +
                "Teslim Edilecek kişi: " + deliveryAddress.getUser().getName() + " " + deliveryAddress.getUser().getSurname() + "\n" +
                "Teslimat Adresi: " + deliveryAddress.toStringForTurkishAddress() + "\n" +
                "Telefon: " + deliveryAddress.getReceiverPhoneNumber() + "\n" +
                "Faks: " + deliveryAddress.getReceiverPhoneNumber() + "\n" +
                "Eposta/kullanıcı adı" + user.getEmail() + "\n" +
                "\n" +
                "\n" +
                "6. SİPARİŞ VEREN KİŞİ BİLGİLERİ\n" +
                "Ad/Soyad/Unvan: " + user.getName() + "\n" +
                "Adres: " + deliveryAddress.toStringForTurkishAddress() + "\n" +
                "Telefon: " + user.getPhoneNumber() + "\n" +
                "Faks: " + user.getPhoneNumber() + "\n" +
                "Eposta/kullanıcı adı" + user.getEmail() + "\n" +
                "\n" +
                "\n" +
                "7. SÖZLEŞME KONUSU ÜRÜN/ÜRÜNLER BİLGİLERİ\n" +
                "1. Malın /Ürün/Ürünlerin/ Hizmetin temel özelliklerini (türü, miktarı, marka/modeli, rengi, adedi) SATICI’ya ait internet sitesinde yayınlanmaktadır. Satıcı tarafından kampanya düzenlenmiş ise ilgili ürünün temel özelliklerini kampanya süresince inceleyebilirsiniz. Kampanya tarihine kadar geçerlidir.\n" +
                "\n" +
                "7.2. Listelenen ve sitede ilan edilen fiyatlar satış fiyatıdır. İlan edilen fiyatlar ve vaatler güncelleme yapılana ve değiştirilene kadar geçerlidir. Süreli olarak ilan edilen fiyatlar ise belirtilen süre sonuna kadar geçerlidir.\n" +
                "\n" +
                "7.3. Sözleşme konusu mal ya da hizmetin tüm vergiler dâhil satış fiyatı aşağıda gösterilmiştir.\n" +
                "\n" +
                productPart +
                "Ara Toplam(₺): " + order.getTotalPrice() + "\n" +
                "(KDV Dahil) " +
                "Kargo Tutarı\n" +
                "Toplam(₺) :" + order.getTotalPrice() + "\n" +
                "\n" +
                "Ödeme Şekli ve Planı: " + order.getPaymentType().getTurkish() + "\n" +
                "Teslimat Adresi: " + deliveryAddress.toStringForTurkishAddress() + "\n" +
                "Teslim Edilecek kişi: " + deliveryAddress.getUser().getName() + " " + deliveryAddress.getUser().getSurname() + "\n" +
                "Fatura Adresi: " + invoiceAddress.toStringForTurkishAddress() + "\n" +
                "Sipariş Tarihi: " + new Date() + "\n" +
                "Teslimat tarihi: " + "1 hafta içerisinde" + "\n" +
                "Teslim şekli: " + "Kargo" + "\n" +
                "\n" +
                "7.4.  Ürün sevkiyat masrafı olan kargo ücreti ALICI tarafından ödenecektir.\n" +
                "\n" +
                "\n" +
                "8. FATURA BİLGİLERİ\n" +
                "Ad/Soyad/Unvan: " + invoiceAddress.getReceiverName() + " " + invoiceAddress.getReceiverSurname() + "\n" +
                "Adres: " + invoiceAddress.toStringForTurkishAddress() + "\n" +
                "Telefon: " + invoiceAddress.getReceiverPhoneNumber() + "\n" +
                "Faks: " + invoiceAddress.getReceiverPhoneNumber() + "\n" +
                "Eposta/kullanıcı adı: " + user.getEmail() + "\n" +
                "Fatura teslim : Fatura sipariş teslimatı sırasında fatura adresine sipariş ile birlikte \n" +
                "teslim edilecektir.\n" +
                "\n" +
                "\n" +
                "9. GENEL HÜKÜMLER\n" +
                "9.1. ALICI, SATICI’ya ait internet sitesinde sözleşme konusu ürünün temel nitelikleri, satış fiyatı ve ödeme şekli ile teslimata ilişkin ön bilgileri okuyup, bilgi sahibi olduğunu, elektronik ortamda gerekli teyidi verdiğini kabul, beyan ve taahhüt eder. ALICI’nın; Ön Bilgilendirmeyi elektronik ortamda teyit etmesi, mesafeli satış sözleşmesinin kurulmasından evvel, SATICI tarafından ALICI' ya verilmesi gereken adresi, siparişi verilen ürünlere ait temel özellikleri, ürünlerin vergiler dâhil fiyatını, ödeme ve teslimat bilgilerini de doğru ve eksiksiz olarak edindiğini kabul, beyan ve taahhüt eder.\n" +
                "\n" +
                "9.2. Sözleşme konusu her bir ürün, 30 günlük yasal süreyi aşmamak kaydı ile ALICI' nın yerleşim yeri uzaklığına bağlı olarak internet sitesindeki ön bilgiler kısmında belirtilen süre zarfında ALICI veya ALICI’nın gösterdiği adresteki kişi ve/veya kuruluşa teslim edilir. Bu süre içinde ürünün ALICI’ya teslim edilememesi durumunda, ALICI’nın sözleşmeyi feshetme hakkı saklıdır. \n" +
                "\n" +
                "9.3. SATICI, Sözleşme konusu ürünü eksiksiz, siparişte belirtilen niteliklere uygun ve varsa garanti belgeleri, kullanım kılavuzları işin gereği olan bilgi ve belgeler ile teslim etmeyi, her türlü ayıptan arî olarak yasal mevzuat gereklerine göre sağlam, standartlara uygun bir şekilde işi doğruluk ve dürüstlük esasları dâhilinde ifa etmeyi, hizmet kalitesini koruyup yükseltmeyi, işin ifası sırasında gerekli dikkat ve özeni göstermeyi, ihtiyat ve öngörü ile hareket etmeyi kabul, beyan ve taahhüt eder.\n" +
                "\n" +
                "9.4. SATICI, sözleşmeden doğan ifa yükümlülüğünün süresi dolmadan ALICI’yı bilgilendirmek ve açıkça onayını almak suretiyle eşit kalite ve fiyatta farklı bir ürün tedarik edebilir.\n" +
                "\n" +
                "9.5. SATICI, sipariş konusu ürün veya hizmetin yerine getirilmesinin imkânsızlaşması halinde sözleşme konusu yükümlülüklerini yerine getiremezse, bu durumu, öğrendiği tarihten itibaren 3 gün içinde yazılı olarak tüketiciye bildireceğini, 14 günlük süre içinde toplam bedeli ALICI’ya iade edeceğini kabul, beyan ve taahhüt eder. \n" +
                "\n" +
                "9.6. ALICI, Sözleşme konusu ürünün teslimatı için işbu Sözleşme’yi elektronik ortamda teyit edeceğini, herhangi bir nedenle sözleşme konusu ürün bedelinin ödenmemesi ve/veya banka kayıtlarında iptal edilmesi halinde, SATICI’nın sözleşme konusu ürünü teslim yükümlülüğünün sona ereceğini kabul, beyan ve taahhüt eder.\n" +
                "\n" +
                "9.7. ALICI, Sözleşme konusu ürünün ALICI veya ALICI’nın gösterdiği adresteki kişi ve/veya kuruluşa tesliminden sonra ALICI'ya ait kredi kartının yetkisiz kişilerce haksız kullanılması sonucunda sözleşme konusu ürün bedelinin ilgili banka veya finans kuruluşu tarafından SATICI'ya ödenmemesi halinde, ALICI Sözleşme konusu ürünü 3 gün içerisinde nakliye gideri SATICI’ya ait olacak şekilde SATICI’ya iade edeceğini kabul, beyan ve taahhüt eder.\n" +
                "\n" +
                "9.8. SATICI, tarafların iradesi dışında gelişen, önceden öngörülemeyen ve tarafların borçlarını yerine getirmesini engelleyici ve/veya geciktirici hallerin oluşması gibi mücbir sebepler halleri nedeni ile sözleşme konusu ürünü süresi içinde teslim edemez ise, durumu ALICI'ya bildireceğini kabul, beyan ve taahhüt eder. ALICI da siparişin iptal edilmesini, sözleşme konusu ürünün varsa emsali ile değiştirilmesini ve/veya teslimat süresinin engelleyici durumun ortadan kalkmasına kadar ertelenmesini SATICI’dan talep etme hakkını haizdir. ALICI tarafından siparişin iptal edilmesi halinde ALICI’nın nakit ile yaptığı ödemelerde, ürün tutarı 14 gün içinde kendisine nakden ve defaten ödenir. ALICI’nın kredi kartı ile yaptığı ödemelerde ise, ürün tutarı, siparişin ALICI tarafından iptal edilmesinden sonra 14 gün içerisinde ilgili bankaya iade edilir. ALICI, SATICI tarafından kredi kartına iade edilen tutarın banka tarafından ALICI hesabına yansıtılmasına ilişkin ortalama sürecin 2 ile 3 haftayı bulabileceğini, bu tutarın bankaya iadesinden sonra ALICI’nın hesaplarına yansıması halinin tamamen banka işlem süreci ile ilgili olduğundan, ALICI, olası gecikmeler için SATICI’yı sorumlu tutamayacağını kabul, beyan ve taahhüt eder.\n" +
                "\n" +
                "9.9. SATICININ, ALICI tarafından siteye kayıt formunda belirtilen veya daha sonra kendisi tarafından güncellenen adresi, e-posta adresi, sabit ve mobil telefon hatları ve diğer iletişim bilgileri üzerinden mektup, e-posta, SMS, telefon görüşmesi ve diğer yollarla iletişim, pazarlama, bildirim ve diğer amaçlarla ALICI’ya ulaşma hakkı bulunmaktadır. ALICI, işbu sözleşmeyi kabul etmekle SATICI’nın kendisine yönelik yukarıda belirtilen iletişim faaliyetlerinde bulunabileceğini kabul ve beyan etmektedir.\n" +
                "\n" +
                "9.10. ALICI, sözleşme konusu mal/hizmeti teslim almadan önce muayene edecek; ezik, kırık, ambalajı yırtılmış vb. hasarlı ve ayıplı mal/hizmeti kargo şirketinden teslim almayacaktır. Teslim alınan mal/hizmetin hasarsız ve sağlam olduğu kabul edilecektir. Teslimden sonra mal/hizmetin özenle korunması borcu, ALICI’ya aittir. Cayma hakkı kullanılacaksa mal/hizmet kullanılmamalıdır. Fatura iade edilmelidir.\n" +
                "\n" +
                "9.11. ALICI ile sipariş esnasında kullanılan kredi kartı hamilinin aynı kişi olmaması veya ürünün ALICI’ya tesliminden evvel, siparişte kullanılan kredi kartına ilişkin güvenlik açığı tespit edilmesi halinde, SATICI, kredi kartı hamiline ilişkin kimlik ve iletişim bilgilerini, siparişte kullanılan kredi kartının bir önceki aya ait ekstresini yahut kart hamilinin bankasından kredi kartının kendisine ait olduğuna ilişkin yazıyı ibraz etmesini ALICI’dan talep edebilir. ALICI’nın talebe konu bilgi/belgeleri temin etmesine kadar geçecek sürede sipariş dondurulacak olup, mezkur taleplerin 24 saat içerisinde karşılanmaması halinde ise SATICI, siparişi iptal etme hakkını haizdir.\n" +
                "\n" +
                "9.12. ALICI, SATICI’ya ait internet sitesine üye olurken verdiği kişisel ve diğer sair bilgilerin gerçeğe uygun olduğunu, SATICI’nın bu bilgilerin gerçeğe aykırılığı nedeniyle uğrayacağı tüm zararları, SATICI’nın ilk bildirimi üzerine derhal, nakden ve defaten tazmin edeceğini beyan ve taahhüt eder.\n" +
                "\n" +
                "9.13. ALICI, SATICI’ya ait internet sitesini kullanırken yasal mevzuat hükümlerine riayet etmeyi ve bunları ihlal etmemeyi baştan kabul ve taahhüt eder. Aksi takdirde, doğacak tüm hukuki ve cezai yükümlülükler tamamen ve münhasıran ALICI’yı bağlayacaktır.\n" +
                "\n" +
                "9.14. ALICI, SATICI’ya ait internet sitesini hiçbir şekilde kamu düzenini bozucu, genel ahlaka aykırı, başkalarını rahatsız ve taciz edici şekilde, yasalara aykırı bir amaç için, başkalarının maddi ve manevi haklarına tecavüz edecek şekilde kullanamaz. Ayrıca, üye başkalarının hizmetleri kullanmasını önleyici veya zorlaştırıcı faaliyet (spam, virus, truva atı, vb.) işlemlerde bulunamaz.\n" +
                "\n" +
                "9.15. SATICI’ya ait internet sitesinin üzerinden, SATICI’nın kendi kontrolünde olmayan ve/veya başkaca üçüncü kişilerin sahip olduğu ve/veya işlettiği başka web sitelerine ve/veya başka içeriklere link verilebilir. Bu linkler ALICI’ya yönlenme kolaylığı sağlamak amacıyla konmuş olup herhangi bir web sitesini veya o siteyi işleten kişiyi desteklememekte ve Link verilen web sitesinin içerdiği bilgilere yönelik herhangi bir garanti niteliği taşımamaktadır.\n" +
                "\n" +
                "9.16. İşbu sözleşme içerisinde sayılan maddelerden bir ya da birkaçını ihlal eden üye işbu ihlal nedeniyle cezai ve hukuki olarak şahsen sorumlu olup, SATICI’yı bu ihlallerin hukuki ve cezai sonuçlarından ari tutacaktır. Ayrıca; işbu ihlal nedeniyle, olayın hukuk alanına intikal ettirilmesi halinde, SATICI’nın üyeye karşı üyelik sözleşmesine uyulmamasından dolayı tazminat talebinde bulunma hakkı saklıdır.\n" +
                "\n" +
                "\n" +
                "10. CAYMA HAKKI\n" +
                "10.1. ALICI; mesafeli sözleşmenin mal satışına ilişkin olması durumunda, ürünün kendisine veya gösterdiği adresteki kişi/kuruluşa teslim tarihinden itibaren 14 (on dört) gün içerisinde, SATICI’ya bildirmek şartıyla hiçbir hukuki ve cezai sorumluluk üstlenmeksizin ve hiçbir gerekçe göstermeksizin malı reddederek sözleşmeden cayma hakkını kullanabilir. Hizmet sunumuna ilişkin mesafeli sözleşmelerde ise, bu süre sözleşmenin imzalandığı tarihten itibaren başlar. Cayma hakkı süresi sona ermeden önce, tüketicinin onayı ile hizmetin ifasına başlanan hizmet sözleşmelerinde cayma hakkı kullanılamaz. Cayma hakkının kullanımından kaynaklanan masraflar SATICI’ ya aittir. ALICI, iş bu sözleşmeyi kabul etmekle, cayma hakkı konusunda bilgilendirildiğini peşinen kabul eder.\n" +
                "\n" +
                "10.2. Cayma hakkının kullanılması için 14 (ondört) günlük süre içinde SATICI' ya iadeli taahhütlü posta, faks veya eposta ile yazılı bildirimde bulunulması ve ürünün işbu sözleşmede düzenlenen \"Cayma Hakkı Kullanılamayacak Ürünler\" hükümleri çerçevesinde kullanılmamış olması şarttır. Bu hakkın kullanılması halinde, \n" +
                "a) 3. kişiye veya ALICI’ ya teslim edilen ürünün faturası, (İade edilmek istenen ürünün faturası kurumsal ise, iade ederken kurumun düzenlemiş olduğu iade faturası ile birlikte gönderilmesi gerekmektedir. Faturası kurumlar adına düzenlenen sipariş iadeleri İADE FATURASI kesilmediği takdirde tamamlanamayacaktır.)\n" +
                "b) İade formu,\n" +
                "c) İade edilecek ürünlerin kutusu, ambalajı, varsa standart aksesuarları ile birlikte eksiksiz ve hasarsız olarak teslim edilmesi gerekmektedir.\n" +
                "d) SATICI, cayma bildiriminin kendisine ulaşmasından itibaren en geç 10 günlük süre içerisinde toplam bedeli ve ALICI’yı borç altına sokan belgeleri ALICI’ ya iade etmek ve 20 günlük süre içerisinde malı iade almakla yükümlüdür.\n" +
                "e) ALICI’ nın kusurundan kaynaklanan bir nedenle malın değerinde bir azalma olursa veya iade imkânsızlaşırsa ALICI kusuru oranında SATICI’ nın zararlarını tazmin etmekle yükümlüdür. Ancak cayma hakkı süresi içinde malın veya ürünün usulüne uygun kullanılması sebebiyle meydana gelen değişiklik ve bozulmalardan ALICI sorumlu değildir. \n" +
                "f) Cayma hakkının kullanılması nedeniyle SATICI tarafından düzenlenen kampanya limit tutarının altına düşülmesi halinde kampanya kapsamında faydalanılan indirim miktarı iptal edilir.\n" +
                "\n" +
                "\n" +
                "11. CAYMA HAKKI KULLANILAMAYACAK ÜRÜNLER\n" +
                "ALICI’nın isteği veya açıkça kişisel ihtiyaçları doğrultusunda hazırlanan ve geri gönderilmeye müsait olmayan, iç giyim alt parçaları, mayo ve bikini altları, makyaj malzemeleri, tek kullanımlık ürünler, çabuk bozulma tehlikesi olan veya son kullanma tarihi geçme ihtimali olan mallar, ALICI’ya teslim edilmesinin ardından ALICI tarafından ambalajı açıldığı takdirde iade edilmesi sağlık ve hijyen açısından uygun olmayan ürünler, teslim edildikten sonra başka ürünlerle karışan ve doğası gereği ayrıştırılması mümkün olmayan ürünler, Abonelik sözleşmesi kapsamında sağlananlar dışında, gazete ve dergi gibi süreli yayınlara ilişkin mallar, Elektronik ortamda anında ifa edilen hizmetler veya tüketiciye anında teslim edilen gayrimaddi mallar, ile ses veya görüntü kayıtlarının, kitap, dijital içerik, yazılım programlarının, veri kaydedebilme ve veri depolama cihazlarının,  bilgisayar sarf malzemelerinin, ambalajının ALICI tarafından açılmış olması halinde iadesi Yönetmelik gereği mümkün değildir. Ayrıca Cayma hakkı süresi sona ermeden önce, tüketicinin onayı ile ifasına başlanan hizmetlere ilişkin cayma hakkının kullanılması da Yönetmelik gereği mümkün değildir.\n" +
                "Kozmetik ve kişisel bakım ürünleri, iç giyim ürünleri, mayo, bikini, kitap, kopyalanabilir yazılım ve programlar, DVD, VCD, CD ve kasetler ile kırtasiye sarf malzemeleri (toner, kartuş, şerit vb.) iade edilebilmesi için ambalajlarının açılmamış, denenmemiş, bozulmamış ve kullanılmamış olmaları gerekir. \n" +
                "\n" +
                "\n" +
                "12. TEMERRÜT HALİ VE HUKUKİ SONUÇLARI\n" +
                "ALICI, ödeme işlemlerini  kredi kartı ile yaptığı durumda temerrüde düştüğü takdirde, kart sahibi banka ile arasındaki kredi kartı sözleşmesi çerçevesinde faiz ödeyeceğini ve bankaya karşı sorumlu olacağını kabul, beyan ve taahhüt eder. Bu durumda ilgili banka hukuki yollara başvurabilir; doğacak masrafları ve vekâlet ücretini ALICI’dan talep edebilir ve her koşulda ALICI’nın borcundan dolayı temerrüde düşmesi halinde, ALICI, borcun gecikmeli ifasından dolayı SATICI’nın uğradığı zarar ve ziyanını ödeyeceğini kabul, beyan ve taahhüt eder\n" +
                "\n" +
                "\n" +
                "13. YETKİLİ MAHKEME\n" +
                "İşbu sözleşmeden doğan uyuşmazlıklarda şikayet ve itirazlar, aşağıdaki kanunda belirtilen parasal sınırlar dâhilinde tüketicinin yerleşim yerinin bulunduğu veya tüketici işleminin yapıldığı yerdeki tüketici sorunları hakem heyetine veya tüketici mahkemesine yapılacaktır. Parasal sınıra ilişkin bilgiler aşağıdadır:\n" +
                "01/01/2017 tarihinden itibaren geçerli olmak üzere, 2017 yılı için tüketici hakem heyetlerine yapılacak başvurularda değeri:\n" +
                "a) 2.400 (iki bin dört yüz) Türk Lirasının altında bulunan uyuşmazlıklarda ilçe tüketici hakem heyetleri,\n" +
                "b) Büyükşehir statüsünde olan illerde 2.400 (iki bin dört yüz) Türk Lirası ile 3.610 (üç bin altı yüz on) Türk Lirası arasındaki uyuşmazlıklarda il tüketici hakem heyetleri,\n" +
                "c) Büyükşehir statüsünde olmayan illerin merkezlerinde 3.610 (üç bin altı yüz on) Türk Lirasının altında bulunan uyuşmazlıklarda il tüketici hakem heyetleri,\n" +
                "ç) Büyükşehir statüsünde olmayan illere bağlı ilçelerde 2.400 (iki bin dört yüz) Türk Lirası ile 3.610 (üç bin altı yüz on) Türk Lirası arasındaki uyuşmazlıklarda il tüketici hakem heyetleri görevli kılınmışlardır.\n" +
                "İşbu Sözleşme ticari amaçlarla yapılmaktadır.\n" +
                "\n" +
                "\n" +
                "14. YÜRÜRLÜK\n" +
                "ALICI, Site üzerinden verdiği siparişe ait ödemeyi gerçekleştirdiğinde işbu sözleşmenin tüm şartlarını kabul etmiş sayılır. SATICI, siparişin gerçekleşmesi öncesinde işbu sözleşmenin sitede ALICI tarafından okunup kabul edildiğine dair onay alacak şekilde gerekli yazılımsal düzenlemeleri yapmakla yükümlüdür. \n" +
                "SATICI: " + firm.getName() + "\n" +
                "ALICI: " + user.getName() + "\n" +
                "TARİH: " + df.format(new Date());
    }


    public String getPreinformativeAgreement(UUID userId, OrderDto orderDto) {

        Order order = orderMapper.toEntity(orderDto);

        User user = userRepository.findUserById(userId);

        Firm firm = firmRepository.findByName("default firm");

        Address deliveryAddress = order.getDeliveryAddress();

        Address invoiceAddress = order.getInvoiceAddress();

        StringBuilder productPart = new StringBuilder();

        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, new Locale("tr"));

        for (BasketObject object : order.getBasketObjects()) {
            productPart.append("Ürün Açıklaması: ").append(object.getProduct().getName()).append("\n")
                    .append("Adet: ").append(object.getProductQuantity()).append("\n")
                    .append("Birim: ").append(object.getProduct().getUnit().getTurkish()).append("\n")
                    .append("Fiyatı(₺): ").append(object.getProduct().getPrice()).append("\n");
        }

        return
                "ÖN BİLGİLENDİRME FORMU\n" +
                        "\n" +
                        "1.KONU\n" +
                        "\n" +
                        "İşbu Satış Sözleşmesi Ön Bilgi Formu’nun konusu, SATICI' nın, SİPARİŞ VEREN/ALICI' ya satışını yaptığı, aşağıda nitelikleri ve satış fiyatı belirtilen ürün/ürünlerin satışı ve teslimi ile ilgili olarak 6502 sayılı Tüketicilerin Korunması Hakkındaki Kanun - Mesafeli Sözleşmeler Yönetmeliği (RG:27.11.2014/29188) hükümleri gereğince tarafların hak ve yükümlülüklerini kapsamaktadır. İş bu ön bilgilendirme formunu kabul etmekle ALICI, sözleşme konusu siparişi onayladığı takdirde sipariş konusu bedeli ve varsa kargo ücreti, vergi gibi belirtilen ek ücretleri ödeme yükümlülüğü altına gireceğini ve bu konuda bilgilendirildiğini peşinen kabul eder.\n" +
                        "\n" +
                        "2. SATICI BİLGİLERİ\n" +
                        "\n" +
                        "Ünvanı: " + firm.getName() + "\n" +
                        "Adres: " + firm.getAddress() + "\n" +
                        "Telefon: " + firm.getPhoneNo() + "\n" +
                        "Faks: " + firm.getPhoneNo() + "\n" +
                        "Eposta: " + firm.getEmail() + "\n" +
                        "\n" +
                        "3. ALICI BİLGİLERİ(Bundan sonra ALICI olarak anılacaktır.)\n" +
                        "\n" +
                        "Teslim edilecek kişi: " + deliveryAddress.getUser().getName() + "\n" +
                        "Teslimat Adresi: " + deliveryAddress.toStringForTurkishAddress() + "\n" +
                        "Telefon: " + deliveryAddress.getReceiverPhoneNumber() + "\n" +
                        "Faks: " + deliveryAddress.getReceiverPhoneNumber() + "\n" +
                        "Eposta/kullanıcı adı" + user.getEmail() + "\n" +
                        "\n" +
                        "4. SİPARİŞ VEREN BİLGİLERİ(Bundan sonra SİPARİŞ VEREN olarak anılacaktır.)\n" +
                        "\n" +
                        "Ad/Soyad/Unvan: " + user.getName() + "\n" +
                        "Adres: " + deliveryAddress.toStringForTurkishAddress() + "\n" +
                        "Telefon: " + user.getPhoneNumber() + "\n" +
                        "Faks: " + user.getPhoneNumber() + "\n" +
                        "Eposta/kullanıcı adı" + user.getEmail() + "\n" +
                        "\n" +
                        "5. SÖZLEŞME KONUSU ÜRÜN/ÜRÜNLER BİLGİLERİ\n" +
                        "\n" +
                        "5.1Malın / Ürün/Ürünlerin / Hizmetin temel özellikleri (türü, miktarı, marka/modeli, rengi, adedi) SATICI’ya ait internet sitesinde yer almaktadır. Ürünün temel özelliklerini kampanya süresince inceleyebilirsiniz. Kampanya tarihine kadar geçerlidir.\n" +
                        "\n" +
                        "5.2Listelenen ve sitede ilan edilen fiyatlar satış fiyatıdır. İlan edilen fiyatlar ve vaatler güncelleme yapılana ve değiştirilene kadar geçerlidir. Süreli olarak ilan edilen fiyatlar ise belirtilen süre sonuna kadar geçerlidir.\n" +
                        "\n" +
                        "5.3Sözleşme konusu mal ya da hizmetin tüm vergiler dâhil satış fiyatı aşağıdaki tabloda gösterilmiştir.\n" +
                        "\n" +
                        productPart +
                        "Ara Toplam(₺): " + order.getTotalPrice() + "\n" +
                        "(KDV Dahil) " +
                        "Kargo Tutarı\n" +
                        "Toplam(₺) :" + order.getTotalPrice() + "\n" +
                        "\n" +
                        "Ödeme Şekli ve Planı: " + order.getPaymentType().getTurkish() + "\n" +
                        "Teslimat Adresi: " + deliveryAddress.toStringForTurkishAddress() + "\n" +
                        "Teslim Edilecek kişi: " + deliveryAddress.getUser() + "\n" +
                        "Fatura Adresi: " + invoiceAddress.toStringForTurkishAddress() + "\n" +
                        "Sipariş Tarihi: " + new Date() + "\n" +
                        "Teslimat tarihi: " + "1 hafta içerisinde" + "\n" +
                        "Teslim şekli: " + "Kargo" + "\n" +
                        "\n" +
                        "Ürün sevkiyat masrafı olan kargo ücreti ALICI tarafından ödenecektir.\n" +
                        "\n" +
                        "6. GENEL HÜKÜMLER\n" +
                        "\n" +
                        "6.1.ALICI, SATICI’ya ait internet sitesinde sözleşme konusu ürünün temel nitelikleri, satış fiyatı ve ödeme şekli ile teslimata ilişkin ön bilgileri okuyup, bilgi sahibi olduğunu, elektronik ortamda gerekli teyidi verdiğini kabul, beyan ve taahhüt eder. ALICININ; Ön Bilgilendirmeyi elektronik ortamda teyit etmesi, mesafeli satış sözleşmesinin kurulmasından evvel, SATICI tarafından ALICI' ya verilmesi gereken adresi, siparişi verilen ürünlere ait temel özellikleri, ürünlerin vergiler dâhil fiyatını, ödeme ve teslimat bilgilerini de doğru ve eksiksiz olarak edindiğini kabul, beyan ve taahhüt eder.\n" +
                        "\n" +
                        "6.2.Sözleşme konusu her bir ürün, 30 günlük yasal süreyi aşmamak kaydı ile ALICI' nın yerleşim yeri uzaklığına bağlı olarak internet sitesindeki ön bilgiler kısmında belirtilen süre zarfında ALICI veya ALICI’ nın gösterdiği adresteki kişi ve/veya kuruluşa teslim edilir. Bu süre içinde ürünün ALICI’ya teslim edilememesi durumunda, ALICI’nın sözleşmeyi feshetme hakkı saklıdır.\n" +
                        "\n" +
                        "6.3.SATICI, sözleşme konusu ürünü eksiksiz, siparişte belirtilen niteliklere uygun ve varsa garanti belgeleri, kullanım kılavuzları ile teslim etmeyi, her türlü ayıptan arî olarak yasal mevzuat gereklerine sağlam, standartlara uygun bir şekilde işin gereği olan bilgi ve belgeler ile işi doğruluk ve dürüstlük esasları dâhilinde ifa etmeyi, hizmet kalitesini koruyup yükseltmeyi, işin ifası sırasında gerekli dikkat ve özeni göstermeyi, ihtiyat ve öngörü ile hareket etmeyi kabul, beyan ve taahhüt eder.\n" +
                        "\n" +
                        "6.4.SATICI, sözleşmeden doğan ifa yükümlülüğünün süresi dolmadan ALICI’yı bilgilendirmek ve açıkça onayını almak suretiyle eşit kalite ve fiyatta farklı bir ürün tedarik edebilir.\n" +
                        "\n" +
                        "6.5.SATICI, sipariş konusu ürün veya hizmetin yerine getirilmesinin imkânsızlaşması halinde sözleşme konusu yükümlülüklerini yerine getiremezse, bu durumu, öğrendiği tarihten itibaren 3 gün içinde yazılı olarak tüketiciye bildireceğini, 14 günlük süre içinde toplam bedeli ALICI’ya iade edeceğini kabul, beyan ve taahhüt eder.\n" +
                        "\n" +
                        "6.6.ALICI, sözleşme konusu ürünün teslimatı için işbu Ön Bilgilendirme Formunu elektronik ortamda teyit edeceğini, herhangi bir nedenle sözleşme konusu ürün bedelinin ödenmemesi ve/veya banka kayıtlarında iptal edilmesi halinde, SATICI’ nın sözleşme konusu ürünü teslim yükümlülüğünün sona ereceğini kabul, beyan ve taahhüt eder.\n" +
                        "\n" +
                        "6.7.ALICI, Sözleşme konusu ürünün ALICI veya ALICI’nın gösterdiği adresteki kişi ve/veya kuruluşa tesliminden sonra ALICI'ya ait kredi kartının yetkisiz kişilerce haksız kullanılması sonucunda sözleşme konusu ürün bedelinin ilgili banka veya finans kuruluşu tarafından SATICI'ya ödenmemesi halinde, ALICI Sözleşme konusu ürünü 3 gün içerisinde nakliye gideri SATICI’ya ait olacak şekilde SATICI’ya iade edeceğini kabul, beyan ve taahhüt eder.\n" +
                        "\n" +
                        "6.8.SATICI, tarafların iradesi dışında gelişen, önceden öngörülemeyen ve tarafların borçlarını yerine getirmesini engelleyici ve/veya geciktirici hallerin oluşması gibi mücbir sebepler halleri nedeni ile sözleşme konusu ürünü süresi içinde teslim edemez ise, durumu ALICI' ya bildireceğini kabul, beyan ve taahhüt eder. ALICI da siparişin iptal edilmesini, sözleşme konusu ürünün varsa emsali ile değiştirilmesini ve/veya teslimat süresinin engelleyici durumun ortadan kalkmasına kadar ertelenmesini SATICI’ dan talep etme hakkına haizdir. ALICI tarafından siparişin iptal edilmesi halinde ALICI’ nın nakit ile yaptığı ödemelerde, ürün tutarı 14 gün içinde kendisine nakden ve defaten ödenir. ALICI’ nın kredi kartı ile yaptığı ödemelerde ise, ürün tutarı, siparişin ALICI tarafından iptal edilmesinden sonra 14 gün içerisinde ilgili bankaya iade edilir. ALICI, SATICI tarafından kredi kartına iade edilen tutarın banka tarafından ALICI hesabına yansıtılmasına ilişkin ortalama sürecin 2 ile 3 haftayı bulabileceğini, bu tutarın bankaya iadesinden sonra ALICI’ nın hesaplarına yansıması halinin tamamen banka işlem süreci ile ilgili olduğundan, ALICI, olası gecikmeler için SATICI’ yı sorumlu tutamayacağını kabul, beyan ve taahhüt eder.\n" +
                        "\n" +
                        "7. FATURA BİLGİLERİ\n" +
                        "\n" +
                        "Ad/Soyad/Unvan: " + invoiceAddress.getReceiverName() + " " + invoiceAddress.getReceiverSurname() + "\n" +
                        "Adres: " + invoiceAddress.toStringForTurkishAddress() + "\n" +
                        "Telefon: " + invoiceAddress.getReceiverPhoneNumber() + "\n" +
                        "Faks: " + invoiceAddress.getReceiverPhoneNumber() + "\n" +
                        "Eposta/kullanıcı adı: " + user.getEmail() + "\n" +
                        "Fatura teslim : Fatura sipariş teslimatı sırasında fatura adresine sipariş ile birlikte\n" +
                        "teslim edilecektir.\n" +
                        "\n" +
                        "8. CAYMA HAKKI\n" +
                        "\n" +
                        "8.1.ALICI; mal satışına ilişkin mesafeli sözleşmelerde, ürünün kendisine veya gösterdiği adresteki kişi/kuruluşa teslim tarihinden itibaren 14 (on dört) gün içerisinde, SATICI’ya bildirmek şartıyla hiçbir hukuki ve cezai sorumluluk üstlenmeksizin ve hiçbir gerekçe göstermeksizin malı reddederek sözleşmeden cayma hakkını kullanabilir. Hizmet sunumuna ilişkin mesafeli sözleşmelerde ise, bu süre sözleşmenin imzalandığı tarihten itibaren başlar. Cayma hakkı süresi sona ermeden önce, tüketicinin onayı ile hizmetin ifasına başlanan hizmet sözleşmelerinde cayma hakkı kullanılamaz. Cayma hakkının kullanımından kaynaklanan masraflar SATICI’ ya aittir.ALICI, iş bu sözleşmeyi kabul etmekle, cayma hakkı konusunda bilgilendirildiğini peşinen kabul eder.\n" +
                        "\n" +
                        "8.2.Cayma hakkının kullanılması için 14 (ondört) günlük süre içinde SATICI' ya iadeli taahhütlü posta, faks veya eposta ile yazılı bildirimde bulunulması ve ürünün işbu sözleşmede düzenlenen düzenlenen \"Cayma Hakkı Kullanılamayacak Ürünler\" hükümleri çerçevesinde kullanılmamış olması şarttır. Bu hakkın kullanılması halinde,\n" +
                        "\n" +
                        "8.2.13. kişiye veya ALICI’ ya teslim edilen ürünün faturası, (İade edilmek istenen ürünün faturası kurumsal ise, geri iade ederken kurumun düzenlemiş olduğu iade faturası ile birlikte gönderilmesi gerekmektedir. Faturası kurumlar adına düzenlenen sipariş iadeleri İADE FATURASI kesilmediği takdirde tamamlanamayacaktır.)\n" +
                        "\n" +
                        "8.2.2.İade formu,\n" +
                        "\n" +
                        "8.2.3.İade edilecek ürünlerin kutusu, ambalajı, varsa standart aksesuarları ile birlikte eksiksiz ve hasarsız olarak teslim edilmesi gerekmektedir.\n" +
                        "\n" +
                        "8.2.4.SATICI, cayma bildiriminin kendisine ulaşmasından itibaren en geç 10 günlük süre içerisinde toplam bedeli ve ALICI’ yı borç altına sokan belgeleri ALICI’ ya iade etmek ve 20 günlük süre içerisinde malı iade almakla yükümlüdür.\n" +
                        "\n" +
                        "8.2.5.ALICI’ nın kusurundan kaynaklanan bir nedenle malın değerinde bir azalma olursa veya iade imkânsızlaşırsa ALICI kusuru oranında SATICI’ nın zararlarını tazmin etmekle yükümlüdür. Ancak cayma hakkı süresi içinde malın veya ürünün usulüne uygun kullanılmasın sebebiyle meydana gelen değişiklik ve bozulmalardan ALICI sorumlu değildir.\n" +
                        "\n" +
                        "8.2.6.Cayma hakkının kullanılması nedeniyle SATICI tarafından düzenlenen kampanya limit tutarının altına düşülmesi halinde kampanya kapsamında faydalanılan indirim miktarı iptal edilir.\n" +
                        "\n" +
                        "9. CAYMA HAKKI KULLANILAMAYACAK ÜRÜNLER\n" +
                        "\n" +
                        "\n" +
                        "9.1.a) Fiyatı finansal piyasalardaki dalgalanmalara bağlı olarak değişen ve satıcı veya sağlayıcının kontrolünde olmayan mal veya hizmetlere ilişkin sözleşmeler.\n" +
                        "\n" +
                        "b) Tüketicinin istekleri veya kişisel ihtiyaçları doğrultusunda hazırlanan mallara ilişkin sözleşmeler.\n" +
                        "\n" +
                        "c) Çabuk bozulabilen veya son kullanma tarihi geçebilecek malların teslimine ilişkin sözleşmeler.\n" +
                        "\n" +
                        "ç) Tesliminden sonra ambalaj, bant, mühür, paket gibi koruyucu unsurları açılmış olan mallardan; iadesi sağlık vehijyenaçısından uygun olmayanların teslimine ilişkin sözleşmeler.\n" +
                        "\n" +
                        "d) Tesliminden sonra başka ürünlerle karışan ve doğası gereği ayrıştırılması mümkün olmayan mallara ilişkin sözleşmeler.\n" +
                        "\n" +
                        "e) Malın tesliminden sonra ambalaj, bant, mühür, paket gibi koruyucu unsurları açılmış olması halinde maddi ortamda sunulan kitap, dijital içerik ve bilgisayar sarf malzemelerine, veri kaydedebilme ve veri depolama cihazlarına ilişkin sözleşmeler.\n" +
                        "\n" +
                        "f) Abonelik sözleşmesi kapsamında sağlananlar dışında, gazete ve dergi gibi süreli yayınların teslimine ilişkin sözleşmeler.\n" +
                        "\n" +
                        "g) Belirli bir tarihte veya dönemde yapılması gereken, konaklama, eşya taşıma, araba kiralama, yiyecek-içecek tedariki ve eğlence veya dinlenme amacıyla yapılan boş zamanın değerlendirilmesine ilişkin sözleşmeler.\n" +
                        "\n" +
                        "ğ) Elektronik ortamda anında ifa edilen hizmetler veya tüketiciye anında teslim edilengayrimaddimallara ilişkin sözleşmeler.\n" +
                        "\n" +
                        "h) Cayma hakkı süresi sona ermeden önce, tüketicinin onayı ile ifasına başlanan hizmetlere ilişkin sözleşmeler.\n" +
                        "\n" +
                        "Kozmetik ve kişisel bakım ürünleri, iç giyim ürünleri, mayo, bikini, kitap, kopyalanabilir yazılım ve programlar, DVD, VCD, CD ve kasetler ile kırtasiye sarf malzemeleri (toner, kartuş, şerit vb.) iade edilebilmesi için ambalajlarının açılmamış, denenmemiş, bozulmamış ve kullanılmamış olmaları gerekir.\n" +
                        "\n" +
                        "9.2. ALICI, şikâyet ve itirazları konusunda başvurularını, aşağıdaki kanunda belirtilen parasal sınırlar dâhilinde tüketicinin yerleşim yerinin bulunduğu veya tüketici işleminin yapıldığı yerdeki tüketici sorunları hakem heyetine veya tüketici mahkemesine yapabilir. Parasal sınıra ilişkin bilgiler aşağıdadır:\n" +
                        "\n" +
                        "01/01/2017 tarihinden itibaren geçerli olmak üzere, 2017 yılı için tüketici hakem heyetlerine yapılacak başvurularda değeri:\n" +
                        "\n" +
                        "a) 2.400 (iki bin dört yüz) Türk Lirasının altında bulunan uyuşmazlıklarda ilçe tüketici hakem heyetleri,\n" +
                        "\n" +
                        "b) Büyükşehir statüsünde olan illerde 2.400 (iki bin dört yüz) Türk Lirası ile 3.610 (üç bin altı yüz on) Türk Lirası arasındaki uyuşmazlıklarda il tüketici hakem heyetleri,\n" +
                        "\n" +
                        "c) Büyükşehir statüsünde olmayan illerin merkezlerinde 3.610 (üç bin altı yüz on) Türk Lirasının altında bulunan uyuşmazlıklarda il tüketici hakem heyetleri,\n" +
                        "\n" +
                        "ç) Büyükşehir statüsünde olmayan illere bağlı ilçelerde 2.400 (iki bin dört yüz) Türk Lirası ile 3.610 (üç bin altı yüz on) Türk Lirası arasındaki uyuşmazlıklarda il tüketici hakem heyetleri görevli kılınmışlardır.\n" +
                        "\n" +
                        "İşbu Sözleşme ticari amaçlarla yapılmaktadır.\n" +
                        "\n" +
                        "SATICI: " + firm.getName() + "\n" +
                        "ALICI: " + user.getName() + "\n" +
                        "TARİH: " + df.format(new Date()) + "\n";
    }
}
