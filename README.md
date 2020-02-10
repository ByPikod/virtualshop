# VirtualShop

Merhaba arkadaşlar! Bu yazıda pluginin hangi adımlar izlenerek yapıldığını, komutları, permissionları görebilirsiniz! Umarım plugini beğenmişsinizdir. İyi incelemeler diliyorum <3

Ayrıca <a target="_blank" href="https://www.youtube.com/c/pikod">kanalıma</a> abone olup <a target="_blank" href="https://bit.ly/DCPikod">discord</a> sunucuma gelmeyi unutmayın!

# Komutlar

<ul>
  <li><b>/market:</b> Marketi açar</li>
  <li><b>/vs:</b> Marketi düzenleme menüsünü açar</li>
</ul>

# Permissions

<ul>
  <li>virtualshop.manage <p>Bu yetki genel admin yetkisidir. /vs ile admin menüsünü açabilmeyi ve itemleri kategorileri editleyebilmeli sağlar.</p></li>
  <li>virtualshop.shop <p>Bu yetki herkese verilen /shop ve /market menülerini açmak için gerekli olan yetkidir. Eğer PermissionsEx veya GroupManager pluginlerinden "+" yerine "-" kullanarak virtualshop.shop yazarsanız bir oyuncudan marketi açmayı alabilirsiniz.</p></li>
</ul>

# Pluginin yapılma adımları

<ul>
<li>Başta bir GUI Api yazdım. GuiManager apisi sayesinde kullanım kolaylaştırıldı.</li>
<li>ActionHandler sınıfını EventListener olarak kaydettirdim. İçerisinde GUI'ye tıklandığında tetiklenecek kodların tamamı yazılı.</li>
<li>Yavaş yavaş GUI'leri oluşturmaya başladım ve hepsini bir paket içerisinde topladım.</li>
<li>Yaml'dan bilgileri çekmek için pManager isminde bir sınıf oluşturdum. Şuan da bakıyorum da gereksiz bir sınıf oluşturmuşum. pManager'den data ve lang kontrolünü sağlayıp save alıyorum falan. (Kodlarda yaptığım bir aptallık da eğer data klasöründeki language.yml'de bulunan bir text yoksa otomatik hata veriyor. Bu yüzden language.yml'nin düzgün olduğuna emin olmalısınız.)</li>
</ul>
