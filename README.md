<h1 align="center">나만의 기상캐스터</h1>
<img src="https://user-images.githubusercontent.com/76841455/122673065-b5b4dd80-d209-11eb-90a6-4cb1fde8c39f.png" width="330" height="330" align="left"></img>

🔊 나만의 기상캐스터란?<br/>
나만의 기상캐스터는 기상 및 대기정보를 확인할 수 있는 애플리케이션입니다. 현 위치의 GPS 좌표나 지정한 읍/면/동 위치를 설정해 해당 지역의 기상 및 대기정보를 쉽고 빠르게 확인할 수 있습니다. 사용자가 확인할 수 있는 정보에는 현재 기상 및 대기 정보, 단기 예보, 중기 예보, 위성·레이더 영상, 기상특보 및 예비 특보를 확인할 수 있습니다.<br/>

🤷‍♂️ 나만의 기상캐스터 프로젝트를 시작하게 된 이유?<br/>
이전의 맘따동 프로젝트를 진행하면서 모든 면에서 부족함을 깨닫고 아쉬움도 많이 남았습니다. 안드로이드 개발 경험에 있어서 조금 더 능숙해지고자 하는 생각과 직접 개발한 앱을 실생활에서 써보고 싶다는 생각에 나만의 기상캐스터 프로젝트를 시작하게 되었습니다. 또한 구글 플레이스토어에 애플리케이션을 출시해보고 싶다는 개인적인 욕심과 광고를 통해 단 1$라도 수익실현이 되는지 경험해보기 위한 부가적인 목표도 있었습니다.<br/>

<h2>⚙ 프로젝트 환경</h2>

| 개발 환경 | API | 라이브러리 |
| :--- | :--- | :--- |
| Android Studio | 공공데이터포털 기상청 API 5종 | kr.hyosang.coordinate |
| Java | 한국환경공단 Open API 2종 | GSON |
| Git · GitHub | V World Open API 1종 | |

<h2>🗂 주요 구현 내용</h2>
<ul>
<li>OPEN API를 통한 XML DOM Parsing</li>
<li>위·경도 / GRID XY / TM / WGS84 좌표 처리</li>
<li>Asynctask 활용한 비동기 처리</li>
<li>GSON 라이브러리를 활용한 SharedPreferences 구현</li>
<li>RecyclerView · Drawer · Fragment 등을 활용한 레이아웃 구현</li>
</ul>

<h2>📸 스크린샷</h2>
<p align="center">
<img src="https://user-images.githubusercontent.com/76841455/103441293-43b6ff00-4c90-11eb-9282-132532bb0314.jpg" width="150"></img>
<img src="https://user-images.githubusercontent.com/76841455/103441295-444f9580-4c90-11eb-8733-80d2603050e4.jpg" width="150"></img>
<img src="https://user-images.githubusercontent.com/76841455/103441296-444f9580-4c90-11eb-9afe-30e250444e34.jpg" width="150"></img>
<img src="https://user-images.githubusercontent.com/76841455/103441297-44e82c00-4c90-11eb-8d36-a851943c15c1.jpg" width="150"></img>
<img src="https://user-images.githubusercontent.com/76841455/103441298-4580c280-4c90-11eb-9c56-67d8b0e6cdc6.jpg" width="150"></img>
</p>
<p align="center">
<img src="https://user-images.githubusercontent.com/76841455/103441299-46195900-4c90-11eb-8cab-3a4f159f622c.jpg" width="150"></img>
<img src="https://user-images.githubusercontent.com/76841455/103441301-46b1ef80-4c90-11eb-8792-33dcd048f15f.jpg" width="150"></img>
<img src="https://user-images.githubusercontent.com/76841455/103441303-46b1ef80-4c90-11eb-8b61-d50df9174722.jpg" width="150"></img>
<img src="https://user-images.githubusercontent.com/76841455/103441304-474a8600-4c90-11eb-912f-db5ded953a84.jpg" width="150"></img>
<img src="https://user-images.githubusercontent.com/76841455/103441305-47e31c80-4c90-11eb-9603-e6148e7f539a.jpg" width="150"></img>
</p>

<h2>▶ 구글 플레이스토어</h2>

👉 [구글 플레이스토어 링크](https://play.google.com/store/apps/details?id=com.bh.myownweathercaster)<br/>
✔ minSdkVersion 26<br/>
✔ targetSdkVersion 29
