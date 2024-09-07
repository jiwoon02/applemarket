import React, { useEffect, useState } from 'react';
import SendbirdApp from '@sendbird/uikit-react/App';
import { useSendbirdStateContext } from '@sendbird/uikit-react';
import './App.css';

const MyChannelList = ({ setChannelData }) => {
  const [channelInfo, setChannelInfo] = useState(null);
  const globalStore = useSendbirdStateContext();
  const sdk = globalStore?.stores?.sdkStore?.sdk;

  useEffect(() => {
    console.log('Fetching data for MyChannelList...'); // Fetch 시작 로그

    fetch('/chatroom/chatdataList')
      .then((response) => response.json())
      .then((data) => {
        console.log('Fetched MyChannelList data:', data);  // Fetch 결과 확인

        // 부모 컴포넌트의 상태 업데이트
        setChannelData({
          appId: data.appId,
          buyerId: data.buyerId,
          buyerNickname: data.buyerNickname,
        });

        // 로컬 상태 업데이트
        setChannelInfo(data);
      })
      .catch((error) => console.error('Error fetching MyChannelList data:', error));
  }, [setChannelData]);

  // SendBird SDK가 준비된 후에 채널 삭제 이벤트 핸들러 등록
  useEffect(() => {
    if (sdk && sdk.GroupChannelHandler) {
      const handler = new sdk.GroupChannelHandler();

      handler.onChannelDeleted = (channelUrl) => {
        console.log(`Channel deleted: ${channelUrl}`);
        // 삭제된 채널을 상태에서 제거
        setChannelInfo((prev) => ({
          ...prev,
          channels: prev.channels.filter(channel => channel.url !== channelUrl),
        }));
      };

      sdk.groupChannel.addGroupChannelHandler('unique_handler_id', handler);

      // 클린업: 컴포넌트가 언마운트될 때 핸들러 제거
      return () => {
        sdk.groupChannel.removeGroupChannelHandler('unique_handler_id');
      };
    }
  }, [sdk]);

  // 데이터가 로드되기 전에는 로딩 상태를 표시합니다.
  if (!channelInfo) {
    return <p>Loading...</p>;
  }

  return (
    <div style={{width:'70vw', height:'100vh'}}>
      <SendbirdApp 
        appId={channelInfo.appId}
        userId={channelInfo.buyerId}
        nickname={channelInfo.buyerNickname}
      />
    </div>
  );
};

export default MyChannelList;