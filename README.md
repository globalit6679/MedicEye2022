# 💊 Medic Eyelet (Medic Eye)

> **시력취약계층을 위한 의약품(편의점 상비약) 정보 안내 애플리케이션**

![Project Banner](https://via.placeholder.com/1000x300?text=Medic+Eyelet)

## 📖 Project Overview (프로젝트 개요)

**Medic Eyelet**은 시력취약계층이 편의점 상비약과 같은 의약품을 안전하게 복용할 수 있도록 돕는 안드로이드 애플리케이션입니다. 스마트폰 카메라를 통해 의약품을 촬영하면 어떤 약인지 인식하여 알려주고, 복용량 및 주의사항과 같은 필수 정보를 음성으로 제공하여 오남용 사고를 예방하는 것을 목표로 합니다.

### 🛑 Background (개발 배경)
* **의약품 구분의 어려움:** 상비약들의 모양이 비슷하여 시각장애인이 구분하기 어렵습니다.
* **정보 접근성 부족:** 편의점 등에서 구매 시 정보를 알려주는 사람이 없어 복용량이나 주의사항을 파악하기 힘듭니다.
* **점자 표기 미비:** 시중 판매 의약품 중 점자 표기가 된 제품은 소수에 불과하며, 표기법 또한 통일되지 않아 위험합니다.

---

## 🛠 Tech Stack (기술 스택)

### Mobile & AI
![Android](https://img.shields.io/badge/Android-Studio-3DDC84?logo=android) ![TensorFlow](https://img.shields.io/badge/TensorFlow-Lite-FF6F00?logo=tensorflow) ![OpenCV](https://img.shields.io/badge/LabelImg-PascalVOC-5C3EE8)

### Backend & Database
![Firebase](https://img.shields.io/badge/Firebase-Realtime_DB-FFCA28?logo=firebase)

### Voice Interface
![TTS](https://img.shields.io/badge/Android-TextToSpeech-3DDC84) ![STT](https://img.shields.io/badge/Android-SpeechToText-3DDC84)

---

## ⚙️ Key Features (핵심 기능)

### 1. AI 기반 의약품 인식 (Object Detection)
* **기능:** 스마트폰 카메라로 의약품을 촬영하면 해당 의약품이 무엇인지 식별합니다.
* **기술:** TensorFlow Object Detection API와 MobileNet 모델을 사용하여 학습 및 구현되었습니다.
* **데이터:** LabelImg를 사용하여 Pascal VOC 형태로 데이터를 레이블링하고 학습시켰습니다.

### 2. 음성 인터페이스 (Voice Interface)
* **음성 검색 (STT):** `RecognitionListener`를 활용하여 사용자가 음성으로 의약품 이름을 검색할 수 있습니다.
* **음성 안내 (TTS):** `TextToSpeech` 기능을 통해 검색 결과, 복용량, 주의사항 등의 텍스트 정보를 음성으로 읽어줍니다.
* **접근성 (Accessibility):** `Content Description`을 활용한 대체 텍스트를 제공하여 위젯 정보 등을 음성으로 안내합니다.

### 3. 의약품 정보 제공 (Drug Information)
* **정보 제공:** 인식되거나 검색된 의약품의 '복용량'과 '주의사항' 정보를 제공합니다.
* **DB 연동:** Firebase Realtime Database를 구축하여 의약품 데이터(사진, 복용법, 주의사항)를 실시간으로 요청하고 받아옵니다.

---

## 📱 Implementation Details (구현 상세)

### 시스템 차별점 (vs 유사 앱)
| 구분 | Medic Eye (본 프로젝트) | HIRA (심평원 앱) | 설리반+ |
|:---:|:---|:---|:---|
| **인식 방식** | **Object Detection** (카메라 촬영) | 바코드 인식 (시각장애인 사용 난해) | 사물 인식 기능 제공 |
| **음성 지원** | **TTS, STT, 대체텍스트** 모두 지원 | 음성 인터페이스 미지원 | 음성 인터페이스 지원 |
| **정보 제공** | **복용량, 주의사항** 등 상세 정보 제공 | 복용량, 주의사항 제공 | 구체적 의약품 정보 미제공 |

### 시연 프로세스
1.  **인식:** 카메라로 의약품 인식 후 버튼을 눌러 검색.
2.  **검색:** 텍스트 입력 또는 음성 검색 기능을 통해 의약품 탐색.
3.  **정보 확인:** '복용량', '주의사항' 버튼을 누르면 해당 내용을 음성으로 안내.

---

## 🔮 Future Plans (향후 계획)
* 아침, 점심, 저녁 약을 구분하여 안내하는 기능을 추가할 예정입니다.
* 데이터베이스 업데이트를 통해 지원하는 의약품의 종류를 늘릴 계획입니다.

---

## 👥 Team Members (팀원)
* **이효원** (20190753)
* **임소연** (20190754)

---
