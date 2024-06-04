-- 아이템 조회
local itemJson = redis.call('get', "30")

-- 아이템이 없을 경우 에러 발생
if not itemJson then
    error("not find item")
end

-- JSON 형태의 아이템 정보를 Lua 테이블로 파싱
local item = cjson.decode(itemJson)

-- 재고(stock) 값을 가져옴
local stock = tonumber(item['stock'])

-- 만약 stock 이 1보다 작은 경우라면 예외 발생
if stock < 1 then
    error("not enough stocks")
end

-- hincryby 명령어를 활용하여 재고를 -1 감소시킵니다.
redis.call('hincrby', KEYS[1], 'stock', -1)

-- 차감 이후 남아있는 재고를 반환합니다.
return stock - 1
